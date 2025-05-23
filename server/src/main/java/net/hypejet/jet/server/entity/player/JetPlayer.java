package net.hypejet.jet.server.entity.player;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquirable;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.object.notnull.WriteNotNullObjectAcquisition;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquirable;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.coordinate.Vector;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.acquisition.gamemode.GameModeAcquisition;
import net.hypejet.jet.entity.acquisition.gamemode.WriteGameModeAcquisition;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.settings.ChangeSettingsEvent;
import net.hypejet.jet.event.events.world.InitialSpawnEvent;
import net.hypejet.jet.scoreboard.position.ScoreboardPosition;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.acquisition.gamemode.GameModeAcquirable;
import net.hypejet.jet.server.entity.acquisition.respawn.WriteRespawnScreenEnabledAcquisition;
import net.hypejet.jet.server.entity.player.movement.PlayerMovementHandler;
import net.hypejet.jet.server.entity.player.spawn.DeathLocation;
import net.hypejet.jet.server.entity.player.spawn.PlayerSpawnInfo;
import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.handler.NetworkDisconnectionHandler;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacketRegistry;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPluginMessagePacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerJoinGamePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerRespawnPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSetObjectiveDisplayedPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.network.session.data.ConfigurationData;
import net.hypejet.jet.server.network.session.data.LoginData;
import net.hypejet.jet.server.network.session.pack.ResourcePackHandler;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.game.audience.PacketReceivingCommonAudience;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.handler.ChunkBatchHandler;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents an implementation of {@linkplain Player a player}.
 *
 * @since 1.0
 * @see Player
 * @see JetEntity
 */
public final class JetPlayer extends JetEntity implements Player, NetworkDisconnectionHandler,
        PacketReceivingCommonAudience {

    private static final Key ENTITY_TYPE = Key.key("player");

    private final SocketPlayerConnection connection;

    private final ChunkBatchHandler chunkBatchHandler;
    private final PlayerMovementHandler movementHandler = new PlayerMovementHandler(this);

    private final NotNullObjectAcquirable<Settings> settings;
    private final NotNullObjectAcquirable<String> clientBrand;

    private final GameModeAcquirable gameMode;
    private final BooleanAcquirable respawnScreenEnabled;

    private final NullableObjectAcquirable<DeathLocation> lastDeathLocation = new NullableObjectAcquirable<>(); // TODO: Updating

    private final Map<ScoreboardPosition, String> objectivesDisplayed = new HashMap<>();
    private final ReentrantReadWriteLock objectivesDisplayedLock = new ReentrantReadWriteLock();

    /**
     * Constructs the {@linkplain JetPlayer player}.
     *
     * @param uniqueId a unique identifier that the player should have
     * @param username a username that the player should have
     * @param connection a player connection that the player should be associated with
     * @param world an initial world that the player should spawn in
     * @param position an initial position that the player should spawn at
     * @param enableRespawnScreen whether respawn screen should be initially enabled for the player
     * @param previousGameMode a game mode that the player had before joining the server, {@code null} if none
     * @param gameMode an initial game mode that the player should have
     * @param settings initial settings of a client associated with the connection
     * @param clientBrand a brand name of a client associated with the player
     * @since 1.0
     */
    private JetPlayer(@NonNull UUID uniqueId, @NonNull String username, @NonNull SocketPlayerConnection connection,
                      @NonNull JetWorld world, @NonNull Position position, boolean enableRespawnScreen,
                      Player.@Nullable GameMode previousGameMode, Player.@NonNull GameMode gameMode,
                      Player.@NonNull Settings settings, @NonNull String clientBrand) {
        super(ENTITY_TYPE, uniqueId, Pointers.builder()
                .withStatic(Identity.UUID, NullabilityUtil.requireNonNull(uniqueId, "unique identifier"))
                .withStatic(Identity.NAME, NullabilityUtil.requireNonNull(username, "username"))
                .build(), position, world);

        this.connection = NullabilityUtil.requireNonNull(connection, "connection");

        this.settings = new NotNullObjectAcquirable<>(NullabilityUtil.requireNonNull(settings, "settings"));
        this.clientBrand = new NotNullObjectAcquirable<>(NullabilityUtil.requireNonNull(clientBrand, "client brand"));

        this.gameMode = new GameModeAcquirable(this, gameMode, previousGameMode);
        this.respawnScreenEnabled = new BooleanAcquirable(enableRespawnScreen);

        connection.initializePlayer(this);
        this.server().registerPlayer(this);

        this.sendJoinGamePacket(world);
        world.addPlayer(this);

        this.chunkBatchHandler = new ChunkBatchHandler(this, position);
        this.chunkBatchHandler.scheduleTask();

        this.sendSpawnPackets(world, position);
        this.server().eventNode().call(new InitialSpawnEvent(this, world, position));
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
    public @NonNull NotNullObjectAcquisition<Settings> settings() {
        return this.settings.acquireRead();
    }

    @Override
    public @NonNull NotNullObjectAcquisition<String> clientBrand() {
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
    public @NonNull ResourcePackHandler resourcePackHandler() {
        return this.connection.resourcePackHandler();
    }

    @Override
    public @NonNull GameModeAcquisition acquireGameModeRead() {
        return this.gameMode.acquireRead();
    }

    @Override
    public @NonNull WriteGameModeAcquisition acquireGameModeWrite() {
        return this.gameMode.acquireWrite();
    }

    @Override
    public @NonNull BooleanAcquisition acquireRespawnScreenEnabledRead() {
        return this.respawnScreenEnabled.acquireRead();
    }

    @Override
    public @NonNull WriteBooleanAcquisition acquireRespawnScreenEnabledWrite() {
        return new WriteRespawnScreenEnabledAcquisition(this, this.respawnScreenEnabled.acquireWrite());
    }

    @Override
    public @Nullable String getDisplayedObjective(@NonNull ScoreboardPosition position) {
        NullabilityUtil.requireNonNull(position, "position");
        try {
            this.objectivesDisplayedLock.readLock().lock();
            return this.objectivesDisplayed.get(position);
        } finally {
            this.objectivesDisplayedLock.readLock().unlock();
        }
    }

    @Override
    public @Nullable String setDisplayedObjective(@NonNull ScoreboardPosition position, @NonNull String name) {
        return this.server().scoreboard().setDisplayedObjective(
                this, position, name,
                this.objectivesDisplayed, this.objectivesDisplayedLock
        );
    }

    @Override
    public @Nullable String removeDisplayedObjective(@NonNull ScoreboardPosition position) {
        NullabilityUtil.requireNonNull(position, "position");
        try {
            this.objectivesDisplayedLock.writeLock().lock();
            String removedObjectiveName = this.objectivesDisplayed.remove(position);
            if (removedObjectiveName != null)
                this.sendPacket(new ServerSetObjectiveDisplayedPlayPacket(position, null));
            return removedObjectiveName;
        } finally {
            this.objectivesDisplayedLock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeDisplayedObjective(@NonNull ScoreboardPosition position, @NonNull String name) {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(name, "name");

        try {
            this.objectivesDisplayedLock.writeLock().lock();
            boolean result = this.objectivesDisplayed.remove(position, name);
            if (result) this.sendPacket(new ServerSetObjectiveDisplayedPlayPacket(position, null));
            return result;
        } finally {
            this.objectivesDisplayedLock.writeLock().unlock();
        }
    }

    @Override
    public boolean replaceDisplayedObjective(@NonNull ScoreboardPosition position,
                                             @NonNull String name, @NonNull String newName) {
        return this.server().scoreboard().replaceDisplayedObjective(
                this, position, name, newName,
                this.objectivesDisplayed, this.objectivesDisplayedLock
        );
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
        this.server().unregisterPlayer(this);
    }

    @Override
    public void sendPacket(@NonNull ServerPacket packet) {
        this.connection.sendPacket(packet);
    }

    /**
     * Gets {@linkplain ChunkBatchHandler a chunk batch handler}, which sends
     * {@linkplain JetChunk chunks} to this {@linkplain JetPlayer player}.
     *
     * @return the chunk batch handler
     * @since 1.0
     */
    public @NonNull ChunkBatchHandler chunkBatchHandler() {
        return this.chunkBatchHandler;
    }

    /**
     * Gets {@linkplain PlayerMovementHandler a player movement handler} of this {@linkplain JetPlayer player}.
     *
     * @return the player movement handler
     * @since 1.0
     */
    public @NonNull PlayerMovementHandler movementHandler() {
        return this.movementHandler;
    }

    /**
     * Updates {@linkplain Settings settings} of the player.
     *
     * @param settings the new settings
     * @since 1.0
     */
    public void setSettings(@NonNull Settings settings) {
        Objects.requireNonNull(settings, "The settings must not be null");
        try (WriteNotNullObjectAcquisition<Settings> acquisition = this.settings.acquireWrite()) {
            Settings previousSettings = acquisition.get();
            acquisition.set(settings);

            ChangeSettingsEvent event = new ChangeSettingsEvent(this, settings);
            this.server().eventNode().call(event);

            byte viewDistance = settings.viewDistance();
            if (previousSettings.viewDistance() != viewDistance)
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
        try (WriteNotNullObjectAcquisition<String> acquisition = this.clientBrand.acquireWrite()) {
            acquisition.set(clientBrand);
        }
    }

    /**
     * Sends {@linkplain ServerRespawnPlayPacket a respawn play packet} to a client associated
     * with this {@linkplain JetPlayer player}.
     *
     * @param world a world that the player should respawn in
     * @param keepAttributes whether attributes of the player should be kept after the respawn
     * @param keepMetadata whether metadata of the player should be kept after the respawn
     * @since 1.0
     */
    public void sendRespawnPacket(@NonNull JetWorld world, boolean keepAttributes, boolean keepMetadata) {
        try (
                GameModeAcquisition gameModeAcquisition = this.acquireGameModeRead();
                NullableObjectAcquisition<DeathLocation> lastDeathLocation = this.lastDeathLocation.acquireRead()
        ) {
            this.sendPacket(new ServerRespawnPlayPacket(
                    this.createSpawnInfo(
                            world, gameModeAcquisition.get(),
                            gameModeAcquisition.previous(),
                            lastDeathLocation.get()
                    ),
                    keepAttributes, keepMetadata
            ));
        }
    }

    /**
     * Sends additional packets about {@linkplain JetWorld world}, {@linkplain Position position} and a server
     * that this {@linkplain JetPlayer player} should spawn in.
     *
     * @param world the world
     * @param position the position
     * @since 1.0
     */
    public void sendSpawnPackets(@NonNull JetWorld world, @NonNull Position position) {
        // TODO
        this.movementHandler.synchronize(position, Vector.zero(), Set.of());
    }

    /**
     * Handles removal of {@linkplain net.hypejet.jet.scoreboard.objective.ScoreboardObjective a scoreboard objective}
     * from {@linkplain net.hypejet.jet.server.scoreboard.JetScoreboard a scoreboard} visible
     * for this {@linkplain JetPlayer player}.
     *
     * @param name a name of the scoreboard objective
     * @since 1.0
     */
    public void handleScoreboardObjectiveRemoval(@NonNull String name) {
        NullabilityUtil.requireNonNull(name, "name");
        try {
            this.objectivesDisplayedLock.readLock().lock();
            this.objectivesDisplayed.entrySet().removeIf(entry -> entry.getValue().equals(name));
        } finally {
            this.objectivesDisplayedLock.readLock().unlock();
        }
    }

    /**
     * Creates {@linkplain JetPlayer a player} with {@linkplain SocketPlayerConnection a player connection}
     * and {@linkplain ConfigurationData a configuration data} specified.
     *
     * @param connection the player connection
     * @param configurationData the configuration data
     * @return the player
     * @since 1.0
     */
    public static @NonNull JetPlayer create(@NonNull SocketPlayerConnection connection,
                                            @NonNull ConfigurationData configurationData) {
        NullabilityUtil.requireNonNull(connection, "connection");
        NullabilityUtil.requireNonNull(configurationData, "configuration data");

        LoginData loginData = configurationData.loginData();

        return new JetPlayer(
                loginData.uniqueId(), loginData.username(), connection, configurationData.world(),
                configurationData.position(), configurationData.enableRespawnScreen(),
                configurationData.previousGameMode(), configurationData.gameMode(), configurationData.settings(),
                configurationData.clientBrand()
        );
    }

    private void sendJoinGamePacket(@NonNull JetWorld world) {
        JetServerConfiguration configuration = this.server().configuration();
        try (
                BooleanAcquisition enableRespawnScreenAcquisition = this.acquireRespawnScreenEnabledRead();
                GameModeAcquisition gameModeAcquisition = this.gameMode.acquireRead();
                NullableObjectAcquisition<DeathLocation> lastDeathLocation = this.lastDeathLocation.acquireRead()
        ) {
            this.sendPacket(new ServerJoinGamePlayPacket(
                    this.entityId(), configuration.hardcore(), Set.of() /* TODO: Permanent worlds */,
                    configuration.maximumPlayers(), configuration.maximumViewDistance(),
                    configuration.simulationDistance(), configuration.reducedDebugInfo(),
                    enableRespawnScreenAcquisition.get(), configuration.showUnlockedRecipesOnly(),
                    this.createSpawnInfo(
                            world, gameModeAcquisition.get(),
                            gameModeAcquisition.previous(),
                            lastDeathLocation.get()
                    ),
                    configuration.enforceSecureProfile()
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