package net.hypejet.jet.server.network.session.task;

import net.hypejet.concurrency.object.ObjectAcquisition;
import net.hypejet.concurrency.object.WriteObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.pack.FeaturePack;
import net.hypejet.jet.event.events.player.configuration.PlayerConfigurationStartEvent;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket.TagRegistry;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerFeatureFlagsConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerFinishConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveHandler;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveResponseHandler;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetSerializableMinecraftRegistry;
import net.hypejet.jet.server.registry.session.RegistryTagUpdateFunction;
import net.hypejet.jet.server.util.unit.Unit;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
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
public final class ConfigurationTask implements SessionTask, KeepAliveResponseHandler, RegistryTagUpdateFunction {

    private static final long TIME_OUT_DURATION = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    private final BooleanAcquirable tagsSent = new BooleanAcquirable();

    private final JetPlayer player;
    private final KeepAliveHandler keepAliveHandler;

    private final CompletableFuture<ClientKnownPacksConfigurationPacket> knownPacksFuture = new CompletableFuture<>();
    private final CompletableFuture<Unit> acknowledgeFuture = new CompletableFuture<>();

    /**
     * Constructs the {@linkplain ConfigurationTask configuration task}.
     *
     * @param player a player that the session task should be handled for
     * @since 1.0
     */
    public ConfigurationTask(@NonNull JetPlayer player) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
        this.keepAliveHandler = new KeepAliveHandler(player);

        Thread.ofVirtual()
                .name(String.format("Configuration session task - %s", player.username()))
                .uncaughtExceptionHandler(player.connection())
                .start(this::runVirtualThreadTask);
    }

    @Override
    public void handleDisconnection() {
        this.keepAliveHandler.handleDisconnection();
        this.knownPacksFuture.cancel(false);
        this.acknowledgeFuture.cancel(false);
    }

    @Override
    public void handleKeepAliveResponse(long keepAliveIdentifier) {
        this.keepAliveHandler.handleKeepAliveResponse(keepAliveIdentifier);
    }

    @Override
    public void updateTags(@NonNull Runnable tagUpdateTask) {
        try (BooleanAcquisition tagsSentAcquisition = this.tagsSent.acquireWrite()) {
            if (!tagsSentAcquisition.get()) return;
            tagUpdateTask.run();
        }
    }

    /**
     * Handles a client response for {@linkplain ServerKnownPacksConfigurationPacket a known packs packet}.
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

        if (this.acknowledgeFuture.isDone())
            throw new IllegalStateException("The configuration finish has been already acknowledged");
        this.acknowledgeFuture.complete(Unit.INSTANCE);

        // Ensure that no packet from the further session is handled
        connection.clientPacketReader().pausePacketReading();
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

    private void runVirtualThreadTask() {
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

        try {
            ClientKnownPacksConfigurationPacket packet = this.knownPacksFuture.get(TIME_OUT_DURATION, TIME_OUT_UNIT);

            Collection<JetMinecraftRegistry<?>> registries = server.registryManager().getRegistries().values();
            for (JetMinecraftRegistry<?> registry : registries) {
                if (!(registry instanceof JetSerializableMinecraftRegistry<?> serializableRegistry)) continue;
                sendRegistry(this.player, serializableRegistry, packet.featurePacks());
            }

            try (WriteBooleanAcquisition tagsSentAcquisition = this.tagsSent.acquireWrite()) {
                Collection<ObjectAcquisition<TagRegistry>> tagRegistryAcquisitions = new HashSet<>();
                try {
                    for (JetMinecraftRegistry<?> registry : registries)
                        tagRegistryAcquisitions.add(registry.createTagRegistry());

                    Collection<TagRegistry> tagRegistries = new HashSet<>();
                    for (ObjectAcquisition<TagRegistry> tagRegistryAcquisition : tagRegistryAcquisitions)
                        tagRegistries.add(tagRegistryAcquisition.get());

                    this.player.sendPacket(new ServerUpdateTagsPacket(Set.copyOf(tagRegistries)));
                    tagsSentAcquisition.set(true);
                } finally {
                    tagRegistryAcquisitions.forEach(ObjectAcquisition::close);
                }
            }

            SocketPlayerConnection connection = this.player.connection();
            if (!this.keepAliveHandler.stopAndAwaitTermination(TIME_OUT_DURATION, TIME_OUT_UNIT)) {
                connection.close(); // The keep alive handler has timed out
                return;
            }

            try (WriteObjectAcquisition<Session> sessionAcquisition = connection.acquireSessionWrite()) {
                connection.sendPacket(new ServerFinishConfigurationPacket());
                this.acknowledgeFuture.get(TIME_OUT_DURATION, TIME_OUT_UNIT);

                Session playSession = new Session(ProtocolState.PLAY, connection);
                sessionAcquisition.set(playSession);
                playSession.startSession(new PlayTask(this.player));

                connection.clientPacketReader().pausePacketReading();
            }
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