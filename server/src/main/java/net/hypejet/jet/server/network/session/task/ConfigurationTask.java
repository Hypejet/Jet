package net.hypejet.jet.server.network.session.task;

import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.acquisition.MutableAcquisition;
import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.pack.FeaturePack;
import net.hypejet.jet.event.events.player.configuration.PlayerConfigurationStartEvent;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFeatureFlagsConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFinishConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKeepAliveConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKnownPacksConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.TagRegistry;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.acquisition.value.AcquirableValue;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveHandler;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveResponseHandler;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetSerializableMinecraftRegistry;
import net.hypejet.jet.server.registry.session.RegistryTagsUpdater;
import net.hypejet.jet.server.util.unit.Unit;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents {@linkplain SessionTask a session task}, which handles {@linkplain ProtocolState#CONFIGURATION
 * a configuration protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see SessionTask
 */
public final class ConfigurationTask implements SessionTask.VirtualThreadTask, KeepAliveResponseHandler,
        RegistryTagsUpdater {

    private static final long TIME_OUT_DURATION = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    private final AcquirableValue<Session> sessionAcquirableValue;
    private final AcquirableValue<Boolean> tagsSentValue = new AcquirableValue<>(false);

    private final JetPlayer player;
    private final KeepAliveHandler keepAliveHandler;

    private final CompletableFuture<ClientKnownPacksConfigurationPacket> knownPacksFuture = new CompletableFuture<>();
    private final CompletableFuture<Unit> acknowledgeFuture = new CompletableFuture<>();

    private @MonotonicNonNull MutableAcquisition<Session> sessionAcquisition;

    /**
     * Constructs the {@linkplain ConfigurationTask configuration task}.
     *
     * @param player a player that the session task should be handled for
     * @param sessionAcquirableValue an acquirable value of the session
     * @since 1.0
     */
    public ConfigurationTask(@NonNull JetPlayer player, @NonNull AcquirableValue<Session> sessionAcquirableValue) {
        NullabilityUtil.requireNonNull(player, "player");
        player.connection().ensureInEventLoop();

        this.sessionAcquirableValue = NullabilityUtil.requireNonNull(sessionAcquirableValue, "session acquirable");
        this.player = player;
        this.keepAliveHandler = new KeepAliveHandler(player, ServerKeepAliveConfigurationPacket::new);
    }

    @Override
    public void runVirtualThreadTask() {
        this.keepAliveHandler.schedule();

        JetMinecraftServer server = this.player.server();
        server.eventNode().call(new PlayerConfigurationStartEvent(this.player));
        this.player.sendServerBrand(server.brandName());

        Set<FeaturePack> enabledFeaturePacks = this.player.server().registryManager().enabledFeaturePacks();

        Set<Key> featureFlags = new HashSet<>();
        for (FeaturePack featurePack : enabledFeaturePacks)
            featureFlags.addAll(featurePack.requiredFeatureFlags());
        this.player.sendPacket(new ServerFeatureFlagsConfigurationPacket(Set.copyOf(featureFlags)));

        Set<PackInfo> packInfos = new HashSet<>();
        enabledFeaturePacks.forEach(dataPack -> packInfos.add(dataPack.info()));
        this.player.sendPacket(new ServerKnownPacksConfigurationPacket(Set.copyOf(packInfos)));

        ClientKnownPacksConfigurationPacket packet;

        try {
            packet = this.knownPacksFuture.get(TIME_OUT_DURATION, TIME_OUT_UNIT);

            Collection<JetMinecraftRegistry<?>> registries = server.registryManager().getRegistries().values();
            for (JetMinecraftRegistry<?> registry : registries) {
                if (!(registry instanceof JetSerializableMinecraftRegistry<?> serializableRegistry)) continue;
                sendRegistry(this.player, serializableRegistry, packet.featurePacks());
            }

            try (MutableAcquisition<Boolean> tagsSentAcquisition = this.tagsSentValue.acquireMutable()) {
                Collection<Acquisition<TagRegistry>> tagRegistryAcquisitions = new HashSet<>();
                try {
                    for (JetMinecraftRegistry<?> registry : registries)
                        tagRegistryAcquisitions.add(registry.createTagRegistry());

                    Collection<TagRegistry> tagRegistries = new HashSet<>();
                    for (Acquisition<TagRegistry> tagRegistryAcquisition : tagRegistryAcquisitions)
                        tagRegistries.add(tagRegistryAcquisition.get());

                    this.player.sendPacket(new ServerUpdateTagsConfigurationPacket(Set.copyOf(tagRegistries)));
                    tagsSentAcquisition.set(true);
                } finally {
                    tagRegistryAcquisitions.forEach(Acquisition::close);
                }
            }

            if (!this.keepAliveHandler.stopAndAwaitTermination(TIME_OUT_DURATION, TIME_OUT_UNIT)) {
                this.player.connection().close(); // The keep alive handler has timed out
                return;
            }

            this.player.connection().submitToEventLoop(this::finishSession).get();
            this.acknowledgeFuture.get(TIME_OUT_DURATION, TIME_OUT_UNIT);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new RuntimeException("The configuration task has been interrupted", exception);
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during a login task", exception);
        } catch (TimeoutException exception) {
            throw new RuntimeException("The configuration task has timed out", exception);
        } catch (CancellationException exception) {
            // Do nothing, the task has been cancelled due to disconnection
        }
    }

    @Override
    public void handleDisconnection() {
        this.keepAliveHandler.handleDisconnection();
        this.knownPacksFuture.cancel(false);
        this.acknowledgeFuture.cancel(false);

        if (this.sessionAcquisition != null)
            this.sessionAcquisition.close();
    }

    @Override
    public void handleKeepAliveResponse(long keepAliveIdentifier) {
        this.keepAliveHandler.handleKeepAliveResponse(keepAliveIdentifier);
    }

    /**
     * Handles a client response for a {@linkplain ServerKnownPacksConfigurationPacket known packs packet}.
     *
     * @param packet the packet that the client respond with
     * @since 1.0
     */
    public void handleKnownPacks(@NonNull ClientKnownPacksConfigurationPacket packet) {
        if (this.knownPacksFuture.isDone())
            throw new IllegalArgumentException("The known packs future has been already finished");
        this.knownPacksFuture.complete(packet);
    }

    /**
     * Handles an acknowledgement of to the configuration finish from the client.
     *
     * @since 1.0
     */
    public void handleFinishAcknowledge() {
        SocketPlayerConnection connection = this.player.connection();
        connection.ensureInEventLoop();

        if (this.acknowledgeFuture.isDone())
            throw new IllegalStateException("The configuration finish has been already acknowledged");
        this.acknowledgeFuture.complete(Unit.INSTANCE);

        MutableAcquisition<Session> sessionAcquisition = this.sessionAcquisition;
        if (sessionAcquisition == null)
            throw new IllegalArgumentException("The session acquirable has been not acquired");

        try (sessionAcquisition) {
            sessionAcquisition.set(new Session(ProtocolState.PLAY, connection, new PlayTask(this.player)));
        }
    }

    /**
     * Gets {@linkplain JetPlayer a player} that the task is handled for.
     *
     * @return the player
     * @since 1.0
     */
    public @NonNull JetPlayer player() {
        return this.player;
    }

    @Override
    public void synchronizeTags(@NonNull TagRegistry tagRegistry) {
        try (Acquisition<Boolean> tagsSentAcquisition = this.tagsSentValue.acquire()) {
            /* If the tags have been not already sent we do not need to send them, because they are already going to
               be sent, and they should be up-to-date thanks to the acquisition. */
            if (!tagsSentAcquisition.get()) return;
            this.player.sendPacket(new ServerUpdateTagsConfigurationPacket(Set.of(tagRegistry)));
        }
    }

    private void finishSession() {
        this.player.connection().ensureInEventLoop();
        MutableAcquisition<Session> sessionAcquisition = this.sessionAcquirableValue.acquireMutable();
        try {
            this.sessionAcquisition = sessionAcquisition;
            this.player.sendPacket(new ServerFinishConfigurationPacket());
        } catch (Throwable throwable) {
            sessionAcquisition.close();
            throw throwable; // Re-throw the throwable, since it has been not completely handled
        }
    }

    private static <V> void sendRegistry(@NonNull JetPlayer player,
                                         @NonNull JetSerializableMinecraftRegistry<V> registry,
                                         @NonNull Collection<PackInfo> dataPackResponse) {
        Key registryKey = registry.registryKey();
        List<ServerRegistryDataConfigurationPacket.Entry> entries = new ArrayList<>();

        for (RegistryEntry<V> entry : registry.entries()) {
            Key identifier = entry.key();
            PackInfo knownPackInfo = entry.knownPackInfo();

            BinaryTag serializedEntry;
            if (knownPackInfo == null || !dataPackResponse.contains(knownPackInfo))
                serializedEntry = registry.binaryTagCodec().write(entry.value());
            else
                serializedEntry = null; // The client already knows the value of the entry

            entries.add(new ServerRegistryDataConfigurationPacket.Entry(identifier, serializedEntry));
        }

        player.sendPacket(new ServerRegistryDataConfigurationPacket(registryKey, List.copyOf(entries)));
    }
}