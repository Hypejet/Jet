package net.hypejet.jet.server.entity.player;

import net.hypejet.concurrency.object.notnull.NotNullObjectAcquirable;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.object.notnull.WriteNotNullObjectAcquisition;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquirable;
import net.hypejet.concurrency.object.nullable.NullableObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquirable;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.entity.acquisition.gamemode.GameModeAcquisition;
import net.hypejet.jet.entity.acquisition.gamemode.WriteGameModeAcquisition;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.entity.player.PlayerPreWorldChangeEvent;
import net.hypejet.jet.event.events.entity.player.PlayerWorldChangeEvent;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.scoreboard.Scoreboard;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.acquisition.gamemode.GameModeAcquirable;
import net.hypejet.jet.server.entity.acquisition.respawn.WriteRespawnScreenEnabledAcquisition;
import net.hypejet.jet.server.entity.player.movement.PlayerMovementSynchronizer;
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
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.network.session.data.ConfigurationData;
import net.hypejet.jet.server.network.session.data.LoginData;
import net.hypejet.jet.server.network.session.pack.ResourcePackHandler;
import net.hypejet.jet.server.scoreboard.JetScoreboard;
import net.hypejet.jet.server.util.game.audience.PacketReceivingPlayerAudience;
import net.hypejet.jet.server.util.viewable.JetViewable;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.handler.ChunkBatchHandler;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import net.hypejet.jet.world.coordinate.flag.RelativeFlag;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * An implementation of the {@linkplain Player player}.
 *
 * @since 1.0
 * @see Player
 */
public final class JetPlayer extends JetEntity implements Player, NetworkDisconnectionHandler,
        PacketReceivingPlayerAudience {

    private static final Key ENTITY_TYPE = Key.key("player");
    private static final Logger LOGGER = LoggerFactory.getLogger(JetPlayer.class);

    private final SocketPlayerConnection connection;

    private final ChunkBatchHandler chunkBatchHandler;
    private final PlayerMovementSynchronizer movementSynchronizer = new PlayerMovementSynchronizer(this);

    private final NotNullObjectAcquirable<String> clientBrand;

    private final GameModeAcquirable gameMode;
    private final BooleanAcquirable respawnScreenEnabled;

    private final NullableObjectAcquirable<DeathLocation> lastDeathLocation = new NullableObjectAcquirable<>(); // TODO: Updating
    private final Set<JetViewable> viewedObjects = new HashSet<>();

    private @NonNull JetWorld world;
    private @NonNull JetScoreboard scoreboard;
    private @NonNull Settings settings;

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
     * @param initialScoreboard an initial scoreboard that the player should have
     * @since 1.0
     */
    private JetPlayer(@NonNull UUID uniqueId, @NonNull String username, @NonNull SocketPlayerConnection connection,
                      @NonNull JetWorld world, @NonNull Position position, boolean enableRespawnScreen,
                      @Nullable GameMode previousGameMode, @NonNull GameMode gameMode, @NonNull Settings settings,
                      @NonNull String clientBrand, @NonNull JetScoreboard initialScoreboard) {
        super(ENTITY_TYPE, uniqueId, Pointers.builder()
                .withStatic(Identity.UUID, Objects.requireNonNull(uniqueId, "unique identifier"))
                .withStatic(Identity.NAME, Objects.requireNonNull(username, "username"))
                .build(), position, connection.server());
        this.connection = Objects.requireNonNull(connection, "connection");
        this.settings = Objects.requireNonNull(settings, "settings");
        this.clientBrand = new NotNullObjectAcquirable<>(Objects.requireNonNull(clientBrand, "client brand"));
        this.gameMode = new GameModeAcquirable(this, gameMode, previousGameMode);
        this.respawnScreenEnabled = new BooleanAcquirable(enableRespawnScreen);
        this.world = Objects.requireNonNull(world, "world");
        this.scoreboard = Objects.requireNonNull(initialScoreboard, "initial scoreboard");
        this.chunkBatchHandler = new ChunkBatchHandler(this, position);
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
    public @NonNull Settings settings() {
        return this.settings;
    }

    @Override
    public @NonNull NotNullObjectAcquisition<String> clientBrand() {
        return this.clientBrand.acquireRead();
    }

    @Override
    public void sendPluginMessage(@NonNull Key identifier, byte @NonNull [] data) {
        Objects.requireNonNull(identifier, "identifier");
        Objects.requireNonNull(data, "data");

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
    public JetWorld world() {
        return this.world;
    }

    @Override
    public void teleport(World world) {
        Objects.requireNonNull(world, "world");
        this.teleport(world, world.defaultSpawnPosition());
    }

    @Override
    public void teleport(World world, Position position) {
        this.teleport(world, position, true, true);
    }

    @Override
    public void teleport(World world, Position position, boolean keepAttributes, boolean keepMetadata) {
        // TODO: Ensure integrity with vanilla
        Objects.requireNonNull(world, "world");
        Objects.requireNonNull(position, "position");

        if (!(world instanceof JetWorld validatedWorld))
            throw new IllegalArgumentException("The specified world is not a valid world");
        JetWorld initialWorld = this.world;

        PlayerPreWorldChangeEvent preChangeEvent = new PlayerPreWorldChangeEvent(this, this.world, position);
        this.server().eventNode().call(preChangeEvent);

        if (preChangeEvent.world() instanceof JetWorld validatedEventWorld) {
            validatedWorld = validatedEventWorld;
        } else {
            LOGGER.warn("An invalid world has been specified in an entity" +
                    " pre-world-change event, falling back to the initially specified world");
        }

        this.world.removePlayer(this);
        this.world = validatedWorld;

        // TODO: Handle "keepAttributes" and "keepMetadata" fields when entity system is implemented

        this.sendRespawnPacket(this.world, keepAttributes, keepMetadata);
        this.movementSynchronizer.synchronize(preChangeEvent.getStartingPosition(), Vector.zero(), Set.of());
        this.chunkBatchHandler.resetChunkView();

        this.world.addPlayer(this);
        this.server().eventNode().call(new PlayerWorldChangeEvent(this, initialWorld));
    }

    @Override
    public @NonNull JetScoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public @NonNull JetScoreboard setScoreboard(@NonNull Scoreboard scoreboard) {
        Objects.requireNonNull(scoreboard, "scoreboard");
        if (!(scoreboard instanceof JetScoreboard validatedScoreboard))
            throw new IllegalArgumentException("The scoreboard specified is not a valid scoreboard");

        JetScoreboard currentScoreboard = this.scoreboard;
        if (scoreboard == currentScoreboard) return currentScoreboard;

        currentScoreboard.removeViewer(this);
        validatedScoreboard.addViewer(this);

        this.scoreboard = validatedScoreboard;
        return currentScoreboard;
    }

    @Override
    public void updatePosition(@NonNull Position position, @NonNull Vector velocity,
                               @NonNull Collection<RelativeFlag> flags) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(velocity, "velocity");
        Objects.requireNonNull(flags, "relative flags");
        // The "synchronize" method is going to apply field changes by itself
        this.movementSynchronizer.synchronize(position, velocity, flags);
        this.chunkBatchHandler.updateChunkView();
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
        // TODO: Move disconnection handling and player unregistering to a tick thread
        this.server().ticker().scheduleTask(() -> this.server().playerList().unregisterPlayer(this));
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
     * Gets a {@linkplain PlayerMovementSynchronizer player movement synchronizer}
     * of this {@linkplain JetPlayer player}.
     *
     * @return the player movement synchronizer
     * @since 1.0
     */
    public @NonNull PlayerMovementSynchronizer movementSynchronizer() {
        return this.movementSynchronizer;
    }

    /**
     * Handles an update of clientside {@linkplain Settings settings} of this {@linkplain JetPlayer player}.
     *
     * @param settings the new clientside settings that the player has
     * @since 1.0
     */
    public void setSettings(@NonNull Settings settings) {
        Objects.requireNonNull(settings, "settings");
        this.server().ticker().scheduleTask(() -> {
            this.settings = settings;
            this.chunkBatchHandler.updateChunkView();
        });
    }

    /**
     * Updates a brand name of the client.
     *
     * @param clientBrand the brand name
     * @since 1.0
     */
    public void setClientBrand(@NonNull String clientBrand) {
        Objects.requireNonNull(clientBrand, "client brand");
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
     * Creates a {@linkplain JetPlayer player} that should be associated with
     * the specified {@linkplain SocketPlayerConnection player connection}.
     *
     * @param connection the player connection
     * @param configurationData a data gathered during a configuration state of the specified player connection
     * @param playerFuture the completable future that should be completed when the player is created
     * @since 1.0
     */
    public static void create(@NonNull SocketPlayerConnection connection,
                              @NonNull ConfigurationData configurationData,
                              @NonNull CompletableFuture<JetPlayer> playerFuture) {
        Objects.requireNonNull(connection, "connection");
        Objects.requireNonNull(configurationData, "configuration data");

        JetMinecraftServer server = connection.server();
        LoginData loginData = configurationData.loginData();

        JetPlayer player = new JetPlayer(
                loginData.uniqueId(), loginData.username(), connection, configurationData.world(),
                configurationData.position(), configurationData.enableRespawnScreen(),
                configurationData.previousGameMode(), configurationData.gameMode(), configurationData.settings(),
                configurationData.clientBrand(), configurationData.initialScoreboard()
        );

        server.ticker().scheduleTask(() -> {
            connection.initializePlayer(player);
            server.playerList().registerPlayer(player);
            playerFuture.complete(player);
        });
    }

    /**
     * Sends a {@linkplain ServerJoinGamePlayPacket server join game play packet}
     * to this {@linkplain JetPlayer player}.
     *
     * @since 1.0
     * @see ServerJoinGamePlayPacket
     */
    // TODO: Move to PlayerList#addPlayer
    public void sendJoinGamePacket() {
        this.server().ticker().ensureRunsInTickLoop();
        try (
                BooleanAcquisition enableRespawnScreenAcquisition = this.acquireRespawnScreenEnabledRead();
                GameModeAcquisition gameModeAcquisition = this.gameMode.acquireRead();
                NullableObjectAcquisition<DeathLocation> lastDeathLocation = this.lastDeathLocation.acquireRead()
        ) {
            JetServerConfiguration configuration = this.server().configuration();
            this.sendPacket(new ServerJoinGamePlayPacket(
                    this.entityId(), configuration.hardcore(), Set.of() /* TODO: Permanent worlds */,
                    configuration.maximumPlayers(), configuration.maximumViewDistance(),
                    configuration.simulationDistance(), configuration.reducedDebugInfo(),
                    enableRespawnScreenAcquisition.get(), configuration.showUnlockedRecipesOnly(),
                    this.createSpawnInfo(
                            this.world(),
                            gameModeAcquisition.get(),
                            gameModeAcquisition.previous(),
                            lastDeathLocation.get()
                    ),
                    configuration.enforceSecureProfile()
            ));
        }

    }

    /**
     * Updates {@linkplain Position position} of this {@linkplain JetPlayer player}
     * with the specified value, which comes from the client.
     *
     * @param position the client position value
     * @since 1.0
     */
    public void handlePositionFromClient(@NonNull Position position) {
        this.updateRawPosition(position);
        this.chunkBatchHandler.updateChunkView();
    }

    /**
     * Adds the specified {@linkplain JetViewable viewable object} to a {@linkplain Set set}
     * of {@linkplain JetViewable viewable objects} viewed by this {@linkplain JetPlayer player}.
     *
     * @param viewable the viewable object to add to the viewed object set
     * @throws IllegalStateException if the viewed object set already contains the specified viewable object
     * @since 1.0
     */
    public void addViewedObject(@NonNull JetViewable viewable) {
        this.server().ticker().ensureRunsInTickLoop();
        if (!this.viewedObjects.add(viewable))
            throw new IllegalStateException("This player is already a viewer of the viewable object");
    }

    /**
     * Removes the specified {@linkplain JetViewable viewable object} from a {@linkplain Set set}
     * of {@linkplain JetViewable viewable objects} viewed by this {@linkplain JetPlayer player}.
     *
     * @param viewable the viewable object to remove from the viewed object set
     * @throws IllegalStateException if the viewed object set already does not contain the specified viewable object
     * @since 1.0
     */
    public void removeViewedObject(@NonNull JetViewable viewable) {
        this.server().ticker().ensureRunsInTickLoop();
        if (!this.viewedObjects.remove(viewable))
            throw new IllegalStateException("This player is already not a viewer of the specified viewable object");
    }

    /**
     * Handles a removal of this {@linkplain JetPlayer player}
     * from the {@linkplain JetMinecraftServer server} associated with it.
     *
     * @since 1.0
     */
    public void handleRemoval() {
        this.server().ticker().ensureRunsInTickLoop();

        Iterator<JetViewable> viewedObjectsIterator = this.viewedObjects.iterator();
        while (viewedObjectsIterator.hasNext()) {
            JetViewable viewedObject = viewedObjectsIterator.next();
            viewedObject.handleViewerRemoval(this);
            viewedObjectsIterator.remove();
        }

        this.world.removePlayer(this);
        this.scoreboard.removeViewer(this);
        // TODO: Use a field to mark this player as removed
    }

    /**
     * Tries to cast {@linkplain Player a player} specified to {@linkplain JetPlayer a player implementation}.
     * If it fails, throws an exception with a detailed message.
     *
     * @param player the player
     * @return the player implementation
     * @throws IllegalArgumentException if the player could not be cast to a player implementation
     * @since 1.0
     */
    public static @NonNull JetPlayer cast(@NonNull Player player) {
        if (!(player instanceof JetPlayer validatedPlayer))
            throw new IllegalArgumentException("The player specified is not a valid player");
        return validatedPlayer;
    }

    private @NotNull PlayerSpawnInfo createSpawnInfo(@NotNull JetWorld world, @Nullable GameMode gameMode,
                                                     @Nullable GameMode previousGameMode,
                                                     @Nullable DeathLocation lastDeathLocation) {
        if (gameMode == null)
            throw new IllegalArgumentException("The game mode has not been set");

        Key dimensionTypeKey = world.dimensionType().key();
        return new PlayerSpawnInfo(
                this.server().registryManager().registry(RegistryReference.DIMENSION_TYPE).indexOf(dimensionTypeKey),
                dimensionTypeKey, world.worldData(), gameMode, previousGameMode, lastDeathLocation,
                0 // TODO: Portal cooldowns
        );
    }
}