package net.hypejet.jet.server.network.session.task;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.concurrency.object.WriteObjectAcquisition;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquirable;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.object.nullable.WriteNullableObjectAcquisition;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.configuration.ConfigurationStartEvent;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket.TagRegistry;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerFeatureFlagsConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerFinishConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerResetChatConfigurationPacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.common.CommonSessionPacketHandler;
import net.hypejet.jet.server.network.session.data.ConfigurationData;
import net.hypejet.jet.server.network.session.data.LoginData;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveHandler;
import net.hypejet.jet.server.network.session.pack.ResourcePackHandler;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.function.SynchronizeRegistryTagsFunction;
import net.hypejet.jet.server.scoreboard.JetScoreboard;
import net.hypejet.jet.server.util.NetworkUtil;
import net.hypejet.jet.server.util.game.audience.PacketReceivingCommonAudience;
import net.hypejet.jet.server.util.unit.Unit;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.session.configuration.ConfigurationManager;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.Position;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.resource.ResourcePackStatus;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents {@linkplain SessionTask a session task}, which handles
 * {@linkplain ProtocolState#CONFIGURATION a configuration protocol state}.
 *
 * @since 1.0
 * @see ProtocolState#CONFIGURATION
 * @see SessionTask
 */
public final class ConfigurationSessionTask implements SessionTask, SynchronizeRegistryTagsFunction,
        ConfigurationManager, CommonSessionPacketHandler, PacketReceivingCommonAudience {

    private static final Key SERVER_BRAND_PLUGIN_MESSAGE_KEY = Key.key("brand");

    private static final long TIME_OUT_DURATION = 20;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationSessionTask.class);

    private final SocketPlayerConnection connection;
    private final LoginData loginData;
    private final KeepAliveHandler keepAliveHandler;

    private final CompletableFuture<ClientKnownPacksConfigurationPacket> knownPacksFuture = new CompletableFuture<>();
    private final CompletableFuture<Unit> acknowledgeFuture = new CompletableFuture<>();

    private final NullableObjectAcquirable<Player.Settings> settings = new NullableObjectAcquirable<>();
    private final NullableObjectAcquirable<String> clientBrand = new NullableObjectAcquirable<>();

    private final ReentrantReadWriteLock tagsSentLock = new ReentrantReadWriteLock();
    private boolean tagsSent;

    /**
     * Constructs the {@linkplain ConfigurationSessionTask configuration session task}.
     *
     * @param connection a connection that the session task should be handled for
     * @param loginData a data of the connection set during a login session task
     * @since 1.0
     */
    public ConfigurationSessionTask(@NonNull SocketPlayerConnection connection, @NonNull LoginData loginData) {
        this.connection = Objects.requireNonNull(connection, "connection");
        this.loginData = Objects.requireNonNull(loginData, "login data");
        this.keepAliveHandler = new KeepAliveHandler(this.connection, loginData.username());
    }

    @Override
    public void start() {
        Thread.ofVirtual()
                .name(String.format("Configuration session task thread - %s", this.loginData.username()))
                .uncaughtExceptionHandler(this.connection)
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
    public void handleResourcePackStatus(@NonNull UUID uniqueId, @NonNull ResourcePackStatus status) {
        this.resourcePackHandler().handleState(uniqueId, status, this);
    }

    @Override
    public void resetChat() {
        this.sendPacket(new ServerResetChatConfigurationPacket());
    }

    @Override
    public @NonNull PlayerConnection connection() {
        return this.connection;
    }

    @Override
    public @NonNull ResourcePackHandler resourcePackHandler() {
        return this.connection.resourcePackHandler();
    }

    @Override
    public void handleClientBrand(@NonNull String name) {
        Objects.requireNonNull(name, "name");
        try (WriteNullableObjectAcquisition<String> acquisition = this.clientBrand.acquireWrite()) {
            acquisition.set(name);
        }
    }

    @Override
    public void handleClientInformation(Player.@NonNull Settings settings) {
        Objects.requireNonNull(settings, "settings");
        try (WriteNullableObjectAcquisition<Player.Settings> acquisition = this.settings.acquireWrite()) {
            acquisition.set(settings);
        }
    }

    @Override
    public void disconnect(@NonNull Component reason) {
        this.connection.disconnect(reason);
    }

    @Override
    public void sendPacket(@NonNull ServerPacket packet) {
        this.connection.sendPacket(packet);
    }

    @Override
    public void synchronizeTags(@NonNull ServerUpdateTagsPacket packet) {
        try {
            this.tagsSentLock.readLock().lock();
            /* Synchronization is not needed as tags have not been initialized yet. The tags
               are going to be initialized for the client with acknowledged tag updates. */
            if (!this.tagsSent) return;
            this.connection.sendPacket(packet);
        } finally {
            this.tagsSentLock.readLock().unlock();
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
        if (this.acknowledgeFuture.isDone())
            throw new IllegalStateException("The configuration finish has been already acknowledged");
        this.acknowledgeFuture.complete(Unit.INSTANCE);

        // Ensure that no packet from the further session is handled
        this.connection.clientPacketReader().pausePacketReading();
    }

    private void runVirtualThreadTask() {
        this.keepAliveHandler.schedule();
        JetMinecraftServer server = this.connection.server();

        ConfigurationStartEvent startEvent = new ConfigurationStartEvent(this);
        server.eventNode().call(startEvent);

        World spawningWorld = startEvent.getSpawningWorld();
        if (spawningWorld == null)
            throw new IllegalStateException("The spawning world has not been set");

        if (!(spawningWorld instanceof JetWorld validatedSpawningWorld))
            throw new IllegalArgumentException("The spawning world is not a valid world");

        Position spawningPosition = startEvent.getSpawningPosition();
        if (spawningPosition == null)
            throw new IllegalStateException("The spawning position has not been set");

        JetScoreboard initialScoreboard;
        if (startEvent.getInitialScoreboard() instanceof JetScoreboard validatedScoreboard) {
            initialScoreboard = validatedScoreboard;
        } else {
            initialScoreboard = server.scoreboardManager().defaultScoreboard();
            LOGGER.warn(
                    "The initial scoreboard specified in a configuration-start event for a player with username" +
                            " of {} is not a valid scoreboard, using the default scoreboard instead.",
                    this.loginData.username()
            );
        }

        this.sendServerBrand();

        // TODO: Implement support for other feature flags
        this.sendPacket(new ServerFeatureFlagsConfigurationPacket(Set.of(Key.key("vanilla"))));

        // TODO: Implement support for other feature packs
        List<KnownPack> serverKnownPacks = List.of(new KnownPack("minecraft", "core", server.versionId()));
        this.sendPacket(new ServerKnownPacksConfigurationPacket(serverKnownPacks));

        try {
            ClientKnownPacksConfigurationPacket packet;

            try {
                packet = this.knownPacksFuture.get(TIME_OUT_DURATION, TIME_OUT_UNIT);
            } catch (TimeoutException exception) {
                throw new RuntimeException("The known packs packet has not been sent on time", exception);
            }

            Set<KnownPack> commonKnownPacks = packet.knownPacks().equals(serverKnownPacks)
                    ? Set.copyOf(serverKnownPacks)
                    : Set.of();

            Collection<JetMinecraftRegistry<?>> registries = server.registryManager().registries();
            registries.forEach(registry -> sendRegistry(this.connection, registry, commonKnownPacks));

            try {
                this.tagsSentLock.writeLock().lock();
                Set<TagRegistry> tagRegistries = new HashSet<>();

                registries.forEach(registry -> {
                    /* We can safely create tag registries without tag locking mechanism as no race conditions
                       happen thanks to the "tagsSentLock". If tags of the registry are modified, the lock
                       is still held and the tag update method will attempt to update tags using the "synchronizeTags"
                       method, which uses the same lock. */
                    // FIXME: Sometimes updating may lead to unnecessary sending tag registries with the same contents
                    TagRegistry tagRegistry = registry.createTagRegistry();
                    if (tagRegistry.tags().isEmpty()) return;
                    tagRegistries.add(tagRegistry);
                });

                this.sendPacket(new ServerUpdateTagsPacket(tagRegistries));
                this.tagsSent = true;
            } finally {
                this.tagsSentLock.writeLock().unlock();
            }

            if (!this.keepAliveHandler.stopAndAwaitTermination(TIME_OUT_DURATION, TIME_OUT_UNIT)) {
                this.connection.close(); // The keep alive handler has timed out
                return;
            }

            try (WriteObjectAcquisition<Session> sessionAcquisition = this.connection.acquireSessionWrite()) {
                this.sendPacket(new ServerFinishConfigurationPacket());
                this.acknowledgeFuture.get(TIME_OUT_DURATION, TIME_OUT_UNIT);

                try (
                        NullableObjectAcquisition<Player.Settings> settingsAcquisition = this.settings.acquireRead();
                        NullableObjectAcquisition<String> clientBrandAcquisition = this.clientBrand.acquireRead()
                ) {
                    Player.Settings settings = settingsAcquisition.get();
                    if (settings == null)
                        throw new IllegalStateException("Settings of the client have not been initialized");

                    String clientBrand = clientBrandAcquisition.get();
                    if (clientBrand == null)
                        throw new IllegalStateException("Brand name of the client has not been initialized");

                    sessionAcquisition.set(new Session(
                            ProtocolState.PLAY, this.connection,
                            new PlaySessionTask(
                                    this.connection,
                                    new ConfigurationData(
                                            this.loginData, validatedSpawningWorld, spawningPosition,
                                            startEvent.shouldEnableRespawnScreen(), startEvent.getPreviousGameMode(),
                                            startEvent.getGameMode(), settings, clientBrand, initialScoreboard
                                    )
                            )
                    ));
                }

                this.connection.clientPacketReader().resumePacketReading();
            } catch (TimeoutException exception) {
                throw new RuntimeException(
                        "The configuration session task has not been acknowledged on time",
                        exception
                );
            }
        } catch (ExecutionException exception) {
            throw new RuntimeException("An error occurred during a login task", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        } catch (CancellationException exception) {
            // Do nothing, the task has been cancelled due to disconnection
        }
    }

    private void sendServerBrand() {
        ByteBuf buf = Unpooled.buffer();
        try {
            StringNetworkCodec.INSTANCE.write(buf, this.connection.server().brandName());
            byte[] messageData = NetworkUtil.readRemainingBytes(buf);
            this.sendPluginMessage(SERVER_BRAND_PLUGIN_MESSAGE_KEY, messageData);
        } finally {
            buf.release();
        }
    }

    private static <V> void sendRegistry(@NonNull SocketPlayerConnection connection,
                                         @NonNull JetMinecraftRegistry<V> registry,
                                         @NonNull Collection<KnownPack> knownPackResponse) {
        BinaryTagCodec<V> valueCodec = registry.valueCodec();
        if (valueCodec == null) return;

        List<ServerRegistryDataConfigurationPacket.Entry> entries = new ArrayList<>();
        for (JetMinecraftRegistry.RegistrationInfo<V> registrationInfo : registry.registrationInfos()) {
            KnownPack knownPack = registrationInfo.knownPack();
            BinaryTag serializedValue;

            if (knownPack != null && knownPackResponse.contains(knownPack)) {
                serializedValue = null; // The client already knows the value by enabling the same feature pack
            } else {
                try {
                    serializedValue = valueCodec.encode(registrationInfo.value());
                } catch (Exception exception) {
                    throw new RuntimeException("An error occurred while encoding a registry value", exception);
                }
            }

            entries.add(new ServerRegistryDataConfigurationPacket.Entry(registrationInfo.key(), serializedValue));
        }

        connection.sendPacket(new ServerRegistryDataConfigurationPacket(registry.registryKey(), List.copyOf(entries)));
    }
}