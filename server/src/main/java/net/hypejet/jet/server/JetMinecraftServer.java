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
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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

    private final CompletableFuture<Void> serverReadyFuture = new CompletableFuture<>();
    private final Thread shutdownThread = this.createShutdownThread();

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

        try {
            Runtime.getRuntime().addShutdownHook(this.shutdownThread);
            this.eventNode.call(new ServerInitializedEvent(this));
            this.ticker.start();
            this.networkManager.bind();
            this.eventNode.call(new ServerReadyEvent(this));
            this.serverReadyFuture.complete(null);
        } catch (Throwable throwable) {
            LOGGER.error("An error occurred while making the server ready", throwable);
            this.serverReadyFuture.completeExceptionally(throwable);
            this.shutdown();
        }
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

    @Override
    public void shutdown() {
        try {
            this.shutdownThread.start();
        } catch (IllegalThreadStateException exception) {
            // The shutdown has already been scheduled
        }
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

    private @NonNull Thread createShutdownThread() {
        return Thread.ofVirtual()
                .name("Server shutdown thread")
                // TODO: Replace single-char names with underscores when JDK 25 releases
                .uncaughtExceptionHandler((t, e) -> LOGGER.error("An error occurred while shutting down the server"))
                .unstarted(() -> {
                    try {
                        this.serverReadyFuture.join();
                    } catch (CancellationException | CompletionException exception) {
                        // The server start error exception has already been thrown inside the main thread
                    }

                    LOGGER.info("Shutting down the server...");
                    this.eventNode.call(new ServerShutdownEvent());
                    this.ticker.shutdown();
                    this.networkManager.shutdown();
                    this.pluginManager.shutdown();
                    LOGGER.info("Successfully shut down the server");
                });
    }

    /**
     * Runs the {@linkplain JetMinecraftServer Minecraft server}.
     *
     * @param args arguments that the server application should start with
     * @since 1.0
     */
    public static void main(String[] args) {
        new JetMinecraftServer();
    }
}