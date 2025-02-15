package net.hypejet.jet.server.entity.player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquirable;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.object.nullable.WriteNullableObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.settings.ChangeSettingsEvent;
import net.hypejet.jet.event.events.world.InitialSpawnEvent;
import net.hypejet.jet.event.events.world.PreWorldSwitchEvent;
import net.hypejet.jet.event.events.world.WorldSwitchEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.player.spawn.DeathLocation;
import net.hypejet.jet.server.entity.player.spawn.PlayerSpawnInfo;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacketRegistry;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPluginMessagePacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerJoinGamePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerRespawnPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerWorldEventPlayPacket;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.NetworkUtil;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.JetWorldManager;
import net.hypejet.jet.server.world.handler.ChunkBatchHandler;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.event.events.ChangeGameModeWorldEvent;
import net.hypejet.jet.world.event.events.EnableRespawnScreenWorldEvent;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Represents an implementation of {@linkplain Player a player}.
 *
 * @since 1.0
 * @see Player
 * @see JetEntity
 */
public final class JetPlayer extends JetEntity implements Player, NetworkDisconnectionHandler {

    private static final Key ENTITY_TYPE = Key.key("player");
    private static final Key BRAND_PLUGIN_MESSAGE_IDENTIFIER = Key.key("brand");

    private static final Logger LOGGER = LoggerFactory.getLogger(JetPlayer.class);

    private final SocketPlayerConnection connection;
    private final ChunkBatchHandler chunkBatchHandler = new ChunkBatchHandler(this);

    private final NullableObjectAcquirable<Settings> settings = new NullableObjectAcquirable<>();
    private final NullableObjectAcquirable<String> clientBrand = new NullableObjectAcquirable<>();
    private final NullableObjectAcquirable<JetWorld> world = new NullableObjectAcquirable<>();

    private final NullableObjectAcquirable<DeathLocation> lastDeathLocation = new NullableObjectAcquirable<>(); // TODO: Updating

    private final NullableObjectAcquirable<GameMode> gameMode = new NullableObjectAcquirable<>();
    private final NullableObjectAcquirable<GameMode> previousGameMode = new NullableObjectAcquirable<>();

    private final BooleanAcquirable respawnScreenEnabled = new BooleanAcquirable(true);

    /**
     * Constructs the {@linkplain JetPlayer player}.
     *
     * @param uniqueId a unique identifier of the player
     * @param username a username of the player
     * @param connection a connection of the player
     * @since 1.0
     */
    public JetPlayer(@NonNull UUID uniqueId, @NonNull String username, @NonNull SocketPlayerConnection connection) {
        super(ENTITY_TYPE, uniqueId, Pointers.builder()
                .withStatic(Identity.UUID, NullabilityUtil.requireNonNull(uniqueId, "unique identifier"))
                .withStatic(Identity.NAME, NullabilityUtil.requireNonNull(username, "username"))
                .build());
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
    }

    @Override
    public @NonNull String username() {
        return this.pointers().get(Identity.NAME).orElseThrow();
    }

    @Override
    public @NonNull SocketPlayerConnection connection() {
        return this.connection;
    }

    @Override
    public void disconnect(@NonNull Component reason) {
        this.connection.disconnect(reason);
    }

    @Override
    public @NonNull NullableObjectAcquisition<Settings> settings() {
        return this.settings.acquireRead();
    }

    @Override
    public @NonNull NullableObjectAcquisition<String> clientBrand() {
        return this.clientBrand.acquireRead();
    }

    @Override
    public @NonNull JetMinecraftServer server() {
        return this.connection.server();
    }

    @Override
    public void sendPluginMessage(@NonNull Key identifier, byte @NonNull [] data) {
        NullabilityUtil.requireNonNull(identifier, "identifier");
        NullabilityUtil.requireNonNull(data, "data");

        try (NotNullObjectAcquisition<ProtocolState> acquisition = this.connection.protocolState()) {
            if (!ServerPacketRegistry.isSupported(acquisition.get(), ServerPluginMessagePacket.class))
                throw new IllegalStateException("The operation is not supported at current protocol state");
            this.sendPacket(new ServerPluginMessagePacket(identifier, data));
        }
    }

    @Override
    public @NonNull NullableObjectAcquisition<? extends World> getWorld() {
        return this.world.acquireRead();
    }

    @Override
    public void setWorld(@NonNull World world, @NonNull Position position) {
        this.setWorld(world, position, true, true);
    }

    @Override
    public void setWorld(@NonNull World world, @NonNull Position position,
                         boolean keepAttributes, boolean keepMetadata) {
        if (!(world instanceof JetWorld validatedWorld))
            throw new IllegalArgumentException("The world specified is not a valid world");
        this.setWorld(validatedWorld, position, keepAttributes, keepMetadata);
    }

    @Override
    public @NonNull NullableObjectAcquisition<Player.GameMode> getGameMode() {
        return this.gameMode.acquireRead();
    }

    @Override
    public void setGameMode(Player.@NonNull GameMode gameMode) {
        try (
                WriteNullableObjectAcquisition<GameMode> previousAcquisition = this.previousGameMode.acquireWrite();
                WriteNullableObjectAcquisition<GameMode> gameModeAcquisition = this.gameMode.acquireWrite()
        ) {
            GameMode previousGameMode = gameModeAcquisition.get();
            gameModeAcquisition.set(gameMode);
            previousAcquisition.set(previousGameMode);
            this.sendPacket(new ServerWorldEventPlayPacket(new ChangeGameModeWorldEvent(gameMode)));
            // TODO: Update abilities
        }
    }

    @Override
    public @NonNull BooleanAcquisition respawnScreenEnabled() {
        return this.respawnScreenEnabled.acquireRead();
    }

    @Override
    public void setRespawnScreenEnabled(boolean enabled) {
        try (WriteBooleanAcquisition acquisition = this.respawnScreenEnabled.acquireWrite()) {
            if (acquisition.get() == enabled) return;
            acquisition.set(enabled);
            this.sendPacket(new ServerWorldEventPlayPacket(new EnableRespawnScreenWorldEvent(enabled)));
        }
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
        ServerPacket packet = switch (type) {
            case CHAT -> throw new IllegalArgumentException("Non-system messages are not supported yet");
            case SYSTEM -> new ServerSystemMessagePlayPacket(message, false);
        };
        this.sendPacket(packet);
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        this.sendPacket(new ServerActionBarPlayPacket(message));
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        this.sendPacket(new ServerPlayerListHeaderAndFooterPlayPacket(header, footer));
    }

    @Override
    public void handleDisconnection() {
        this.chunkBatchHandler.handleDisconnection();
    }

    /**
     * Gets {@linkplain ChunkBatchHandler a chunk batch handler}, which sends
     * {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunks} to this {@linkplain JetPlayer player}.
     *
     * @return the chunk batch handler
     * @since 1.0
     */
    public @NonNull ChunkBatchHandler chunkBatchHandler() {
        return this.chunkBatchHandler;
    }

    /**
     * Updates {@linkplain Settings settings} of the player.
     *
     * @param settings the new settings
     * @since 1.0
     */
    public void setSettings(@NonNull Settings settings) {
        Objects.requireNonNull(settings, "The settings must not be null");
        try (WriteNullableObjectAcquisition<Settings> acquisition = this.settings.acquireWrite()) {
            Settings previousSettings = acquisition.get();
            acquisition.set(settings);

            ChangeSettingsEvent event = new ChangeSettingsEvent(this, settings);
            this.server().eventNode().call(event);

            byte viewDistance = settings.viewDistance();
            if (previousSettings == null || previousSettings.viewDistance() != viewDistance)
                this.chunkBatchHandler.handleViewDistanceUpdate(viewDistance);
        }
    }

    /**
     * Updates a brand name of the client.
     *
     * @param clientBrand the brand name
     * @since 1.0
     */
    public void setClientBrand(@NonNull String clientBrand) {
        NullabilityUtil.requireNonNull(clientBrand, "client brand");
        try (WriteNullableObjectAcquisition<String> acquisition = this.clientBrand.acquireWrite()) {
            acquisition.set(clientBrand);
        }
    }

    /**
     * Sends a plugin message containing brand name of the server to a client of this player.
     *
     * @param brand the server brand name
     * @since 1.0
     */
    public void sendServerBrand(@NonNull String brand) {
        ByteBuf buf = Unpooled.buffer();
        StringNetworkCodec.INSTANCE.write(buf, brand);

        byte[] messageData = NetworkUtil.readRemainingBytes(buf);
        this.sendPluginMessage(BRAND_PLUGIN_MESSAGE_IDENTIFIER, messageData);

        buf.release();
    }

    /**
     * Sends a packet to a client backed by {@linkplain SocketPlayerConnection a socket player connection} attached
     * to this player.
     *
     * @param packet the server packet
     * @since 1.0
     * @see SocketPlayerConnection#sendPacket(ServerPacket)
     */
    public void sendPacket(@NonNull ServerPacket packet) {
        this.connection.sendPacket(packet);
    }

    /**
     * Initializes {@linkplain GameMode a game mode} of this {@linkplain Player player}.
     *
     * @param previousGameMode a previous game mode that the player had
     * @param gameMode a game mode that the player should have now
     * @since 1.0
     */
    public void initializeGameMode(@Nullable GameMode previousGameMode, @NonNull GameMode gameMode) {
        NullabilityUtil.requireNonNull(gameMode, "game mode");
        try (
                WriteNullableObjectAcquisition<GameMode> previousAcquisition = this.previousGameMode.acquireWrite();
                WriteNullableObjectAcquisition<GameMode> gameModeAcquisition = this.gameMode.acquireWrite()
        ) {
            previousAcquisition.set(previousGameMode);
            gameModeAcquisition.set(gameMode);
        }
    }

    /**
     * Initializes whether a respawn screen should be enabled for this {@linkplain Player player}.
     *
     * @param enableRespawnScreen {@code true} if the respawn screen should be enabled, {@code false} otherwise
     * @since 1.0
     */
    public void initializeRespawnScreenEnabled(boolean enableRespawnScreen) {
        try (WriteBooleanAcquisition acquisition = this.respawnScreenEnabled.acquireWrite()) {
            acquisition.set(enableRespawnScreen);
        }
    }

    private void setWorld(@NonNull JetWorld world, @NonNull Position position,
                          boolean keepAttributes, boolean keepMetadata) {
        JetWorldManager worldManager = this.server().worldManager();
        BooleanAcquisition newWorldRegisteredAcquisition = null;

        try (
                BooleanAcquisition worldRegisteredAcquisition = worldManager.isRegistered(world);
                BooleanAcquisition chunkBatchTaskRunningAcquisition = this.chunkBatchHandler.isTaskRunning();
                WriteNullableObjectAcquisition<JetWorld> worldAcquisition = this.world.acquireWrite()
        ) {
            if (!worldRegisteredAcquisition.get())
                throw new IllegalArgumentException("The world specified has not been registered in the world manager");

            if (chunkBatchTaskRunningAcquisition.get())
                this.chunkBatchHandler.cancelTask();

            JetWorld previousWorld = worldAcquisition.get();
            EventNode<Object> eventNode = this.server().eventNode();

            boolean firstSpawn = previousWorld == null;
            if (!firstSpawn) {
                previousWorld.removePlayer(this);

                PreWorldSwitchEvent event = new PreWorldSwitchEvent(this, previousWorld, world, position);
                eventNode.call(event);

                if (event.isCancelled())
                    return;

                World newWorld = event.getNewWorld();
                if (newWorld != world) {
                    if (newWorld instanceof JetWorld validatedNewWorld) {
                        newWorldRegisteredAcquisition = worldManager.isRegistered(newWorld);

                        if (newWorldRegisteredAcquisition.get()) {
                            world = validatedNewWorld;
                        } else {
                            LOGGER.warn("The new world specified in the pre-world-switch event has not been" +
                                    " registered in the world manager, switching back to the world specified" +
                                    " as an argument in the method");
                        }
                    } else {
                        LOGGER.warn("The new world specified in the pre-world-switch event is not a valid world," +
                                " switching back to the world specified as an argument in the method");
                    }
                }

                position = event.getStartingPosition();
            }

            worldAcquisition.set(world);

            if (firstSpawn) {
                this.sendJoinGamePacket(world);
                // TODO: Send initial recipes packets, etc.
            } else {
                this.sendRespawnPacket(world, keepAttributes, keepMetadata);
            }

            // TODO: Difficulty packets, etc.

            world.addPlayer(this);

            // TODO: Set position

            this.chunkBatchHandler.scheduleTask(world, position);

            Object postEvent = previousWorld == null
                    ? new InitialSpawnEvent(this, world, position)
                    : new WorldSwitchEvent(this, previousWorld, world, position);
            eventNode.call(postEvent);
        } finally {
            if (newWorldRegisteredAcquisition != null)
                newWorldRegisteredAcquisition.close();
        }
    }

    private void sendJoinGamePacket(@NonNull JetWorld world) {
        JetServerConfiguration configuration = this.server().configuration();
        try (
                BooleanAcquisition enableRespawnScreenAcquisition = this.respawnScreenEnabled.acquireRead();
                NullableObjectAcquisition<GameMode> gameModeAcquisition = this.gameMode.acquireRead();
                NullableObjectAcquisition<GameMode> previousGameModeAcquisition = this.previousGameMode.acquireRead();
                NullableObjectAcquisition<DeathLocation> lastDeathLocation = this.lastDeathLocation.acquireRead()
        ) {
            this.sendPacket(new ServerJoinGamePlayPacket(
                    this.entityId(), configuration.hardcore(), Set.of() /* TODO: Permanent worlds */,
                    configuration.maximumPlayers(), configuration.maximumViewDistance(),
                    configuration.simulationDistance(), configuration.reducedDebugInfo(),
                    enableRespawnScreenAcquisition.get(), configuration.showUnlockedRecipesOnly(),
                    this.createSpawnInfo(
                            world, gameModeAcquisition.get(),
                            previousGameModeAcquisition.get(),
                            lastDeathLocation.get()
                    ),
                    configuration.enforceSecureProfile()
            ));
        }

    }

    private void sendRespawnPacket(@NonNull JetWorld world, boolean keepAttributes, boolean keepMetadata) {
        try (
                NullableObjectAcquisition<GameMode> gameModeAcquisition = this.gameMode.acquireRead();
                NullableObjectAcquisition<GameMode> previousGameModeAcquisition = this.previousGameMode.acquireRead();
                NullableObjectAcquisition<DeathLocation> lastDeathLocation = this.lastDeathLocation.acquireRead()
        ) {
            this.sendPacket(new ServerRespawnPlayPacket(
                    this.createSpawnInfo(
                            world, gameModeAcquisition.get(),
                            previousGameModeAcquisition.get(), lastDeathLocation.get()
                    ),
                    keepAttributes, keepMetadata
            ));
        }
    }

    private @NotNull PlayerSpawnInfo createSpawnInfo(@NotNull JetWorld world, @Nullable GameMode gameMode,
                                                     @Nullable GameMode previousGameMode,
                                                     @Nullable DeathLocation lastDeathLocation) {
        if (gameMode == null)
            throw new IllegalArgumentException("The game mode has not been set");

        JetMinecraftServer server = this.server();
        JetRegistryManager registryManager = server.registryManager();

        JetMinecraftRegistry<DimensionType> dimensionTypeRegistry = registryManager.dimensionTypeRegistry();
        JetRegistryEntry<DimensionType> dimensionType = world.dimensionType();

        return new PlayerSpawnInfo(
                dimensionTypeRegistry.identifierOf(dimensionType), dimensionType.key(), world.worldData(),
                gameMode, previousGameMode, lastDeathLocation, 0 /* TODO: Portal cooldowns */
        );
    }
}