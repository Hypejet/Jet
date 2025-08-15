package net.hypejet.jet.server;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.entity.movement.acquisition.MovementAcquisition;
import net.hypejet.jet.event.events.server.ServerReadyEvent;
import net.hypejet.jet.event.events.server.ServerShutdownEvent;
import net.hypejet.jet.event.events.world.InitialSpawnEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.command.JetCommandManager;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.configuration.unparsed.UnparsedServerConfiguration;
import net.hypejet.jet.server.entity.acquisition.world.EntityWorldAcquisition;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.NetworkManager;
import net.hypejet.jet.server.plugin.JetPluginManager;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.scoreboard.JetScoreboardManager;
import net.hypejet.jet.server.tick.Ticker;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.world.JetWorldManager;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of the {@linkplain MinecraftServer Minecraft server}.
 *
 * @since 1.0
 * @see MinecraftServer
 */
public final class JetMinecraftServer implements MinecraftServer {

    private static final String BRAND_NAME = "Jet";
    private static final Logger LOGGER = LoggerFactory.getLogger(JetMinecraftServer.class);

    private final EventNode<Object> eventNode = new EventNode<>(Object.class);
    private final JetServerConfiguration configuration;

    private final JetCommandManager commandManager;
    private final JetRegistryManager registryManager;
    private final JetPluginManager pluginManager;
    private final JetWorldManager worldManager;
    private final JetScoreboardManager scoreboardManager;

    private final NetworkManager networkManager;
    private final Ticker ticker;

    private final Set<JetPlayer> players = ConcurrentHashMap.newKeySet();

    /**
     * Constructs the {@linkplain JetMinecraftServer Minecraft server}.
     *
     * @since 1.0
     */
    JetMinecraftServer() {
        Set<JetPlayer> unmodifiablePlayersView = Collections.unmodifiableSet(this.players);

        this.configuration = JetServerConfiguration.parse(this, UnparsedServerConfiguration.create());
        this.networkManager = new NetworkManager(this);

        this.pluginManager = new JetPluginManager(this.eventNode);
        this.commandManager = new JetCommandManager(this.eventNode, unmodifiablePlayersView);
        this.registryManager = new JetRegistryManager(this);
        this.worldManager = new JetWorldManager(this);
        this.scoreboardManager = new JetScoreboardManager();
        this.ticker = new Ticker(this);

        this.networkManager.bind();
        this.eventNode.call(new ServerReadyEvent(this));
    }

    @Override
    public @NonNull EventNode<Object> eventNode() {
        return this.eventNode;
    }

    @Override
    public int protocolVersion() {
        return MinecraftVersion.PROTOCOL_VERSION;
    }

    @Override
    public @NonNull String minecraftVersion() {
        return MinecraftVersion.VERSION_NAME;
    }

    @Override
    public @NonNull String brandName() {
        return BRAND_NAME;
    }

    @Override
    public @NonNull JetServerConfiguration configuration() {
        return this.configuration;
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutting down the server...");
        this.eventNode.call(new ServerShutdownEvent());
        this.ticker.shutdown();
        this.networkManager.shutdown();
        this.pluginManager.shutdown();
        LOGGER.info("Successfully shut down the server");
    }

    @Override
    public @NonNull Set<JetPlayer> players() {
        return Set.copyOf(this.players);
    }

    @Override
    public @NonNull JetCommandManager commandManager() {
        return this.commandManager;
    }

    @Override
    public @NonNull JetPluginManager pluginManager() {
        return this.pluginManager;
    }

    @Override
    public @NonNull JetRegistryManager registryManager() {
        return this.registryManager;
    }

    @Override
    public @NonNull JetWorldManager worldManager() {
        return this.worldManager;
    }

    @Override
    public @NonNull JetScoreboardManager scoreboardManager() {
        return this.scoreboardManager;
    }

    /**
     * Gets an identifier of a Minecraft version that the server runs on.
     *
     * @return the Minecraft version identifier
     * @since 1.0
     */
    public @NonNull String versionId() {
        return MinecraftVersion.VERSION_ID;
    }

    /**
     * Gets {@linkplain Ticker a ticker} of the server.
     *
     * @return the ticker
     * @since 1.0
     */
    public @NonNull Ticker ticker() {
        return this.ticker;
    }

    /**
     * Registers the specified {@linkplain JetPlayer player} on the server.
     *
     * @param player the player to register
     * @since 1.0
     */
    public void registerPlayer(@NonNull JetPlayer player) {
        this.ticker.ensureRunsInTickLoop();

        /* A call outside the event loop is safe in this case. When a player gets disconnected, the unregister method
           is going to be called and that method also runs in a tick loop. It means that the unregister method
           cannot be called until this method stops running, therefore no race conditions should happen. */
        if (!player.connection().isActive()) return;

        player.sendJoinGamePacket();
        // TODO: Difficulty packets, ability packets, held slot packets, etc.
        player.getScoreboard().addViewer(player);

        try (
                MovementAcquisition movementAcquisition = player.acquireMovementRead();
                EntityWorldAcquisition<?> worldAcquisition = player.acquireWorldRead()
        ) {
            Position position = movementAcquisition.position();
            player.movementHandler().synchronize(position, Vector.zero(), Set.of());

            this.players.add(player);

            // TODO: Send other world data
            player.chunkBatchHandler().scheduleTask(); // TODO: Ensure that it produces the same behaviour as vanilla

            JetWorld world = worldAcquisition.get();
            world.addPlayer(player);

            this.eventNode.call(new InitialSpawnEvent(player, world, position));
        }
    }

    /**
     * Unregisters the specified {@linkplain JetPlayer player} from the server.
     *
     * @param player the player to unregister
     * @since 1.0
     */
    public void unregisterPlayer(@NonNull JetPlayer player) {
        this.ticker.ensureRunsInTickLoop();
        this.players.remove(player);
    }
}