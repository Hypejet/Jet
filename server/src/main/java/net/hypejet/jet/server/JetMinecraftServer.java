package net.hypejet.jet.server;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.event.events.lifecycle.ServerInitializedEvent;
import net.hypejet.jet.event.events.lifecycle.ServerReadyEvent;
import net.hypejet.jet.event.events.lifecycle.ServerShutdownEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.command.JetCommandManager;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.entity.player.PlayerList;
import net.hypejet.jet.server.network.NetworkManager;
import net.hypejet.jet.server.plugin.JetPluginManager;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.scoreboard.JetScoreboardManager;
import net.hypejet.jet.server.tick.Ticker;
import net.hypejet.jet.server.world.JetWorldManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

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
    private final JetServerConfiguration configuration = JetServerConfiguration.create();

    private final NetworkManager networkManager;
    private final Ticker ticker;
    private final PlayerList playerList;

    private final JetCommandManager commandManager;
    private final JetRegistryManager registryManager;
    private final JetPluginManager pluginManager;
    private final JetWorldManager worldManager;
    private final JetScoreboardManager scoreboardManager;

    /**
     * Constructs the {@linkplain JetMinecraftServer Minecraft server}.
     *
     * @since 1.0
     */
    JetMinecraftServer() {
        this.networkManager = new NetworkManager(this);
        this.ticker = new Ticker(this);
        this.playerList = new PlayerList(this.eventNode, this.ticker);

        this.pluginManager = new JetPluginManager(this.eventNode);
        this.commandManager = new JetCommandManager(this.eventNode, this.playerList);
        this.registryManager = new JetRegistryManager(this.eventNode, this.networkManager);
        this.worldManager = new JetWorldManager(this.registryManager);
        this.scoreboardManager = new JetScoreboardManager();
        this.eventNode.call(new ServerInitializedEvent(this));

        this.ticker.start();
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
        return this.playerList.players();
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
     * Gets a {@linkplain PlayerList player list} of the server.
     *
     * @return the player list
     * @since 1.0
     */
    public @NonNull PlayerList playerList() {
        return this.playerList;
    }
}