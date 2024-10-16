package net.hypejet.jet.server;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.server.ServerReadyEvent;
import net.hypejet.jet.event.events.server.ServerShutdownEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.plugin.PluginManager;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.server.command.JetCommandManager;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.NetworkManager;
import net.hypejet.jet.server.plugin.JetPluginManager;
import net.hypejet.jet.server.util.ServerPingUtil;
import net.hypejet.jet.server.world.JetWorldManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents an implementation of {@linkplain MinecraftServer Minecraft server}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class JetMinecraftServer implements MinecraftServer {

    private static final String BRAND_NAME = "Jet";

    private static final int PROTOCOL_VERSION = 767;
    public static final String MINECRAFT_VERSION = "1.21";

    private static final Logger LOGGER  = LoggerFactory.getLogger(JetMinecraftServer.class);

    private final EventNode<Object> eventNode = EventNode.create();
    private final JetServerConfiguration configuration;

    private final NetworkManager networkManager;
    private final ServerListPing.Favicon serverIcon;

    private final Set<JetPlayer> players = new HashSet<>();
    private final ReentrantReadWriteLock playersLock = new ReentrantReadWriteLock();

    private final JetPluginManager pluginManager;
    private final JetWorldManager worldManager = new JetWorldManager();
    private final JetCommandManager commandManager;

    /**
     * Constructs the {@linkplain JetMinecraftServer Jet Minecraft server}.
     *
     * @since 1.0
     */
    JetMinecraftServer() {
        this.configuration = JetServerConfiguration.create();
        this.serverIcon = ServerPingUtil.loadServerIcon(LOGGER);
        this.commandManager = new JetCommandManager(this);
        this.pluginManager = new JetPluginManager(this);
        this.networkManager = new NetworkManager(this);
        this.eventNode.call(new ServerReadyEvent());
    }

    @Override
    public @NonNull EventNode<Object> eventNode() {
        return this.eventNode;
    }

    @Override
    public int protocolVersion() {
        return PROTOCOL_VERSION;
    }

    @Override
    public @NonNull String minecraftVersion() {
        return MINECRAFT_VERSION;
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
        this.eventNode.addListener(new ServerShutdownEvent());
        this.networkManager.shutdown();
        this.pluginManager.shutdown();
        LOGGER.info("Successfully shut down the server");
    }

    @Override
    public @NonNull Collection<Player> players() {
        this.playersLock.readLock().lock();
        try {
            return Set.copyOf(this.players);
        } finally {
            this.playersLock.readLock().unlock();
        }
    }

    @Override
    public @NonNull Collection<Player> players(@NonNull ProtocolState state) {
        Set<Player> playersCopy = new HashSet<>();

        for (Player player : this.players()) {
            if (player.connection().getProtocolState() != state) continue;
            playersCopy.add(player);
        }

        return Set.copyOf(playersCopy);
    }

    @Override
    public @NonNull JetWorldManager worldManager() {
        return this.worldManager;
    }

    @Override
    public @NonNull JetCommandManager commandManager() {
        return this.commandManager;
    }

    @Override
    public @NonNull PluginManager pluginManager() {
        return this.pluginManager;
    }

    /**
     * Registers a player on the server.
     *
     * @param player the player
     * @since 1.0
     */
    public void registerPlayer(@NonNull JetPlayer player) {
        this.playersLock.writeLock().lock();
        try {
            if (!player.connection().isClosed()) {
                this.players.add(player);
            }
        } finally {
            this.playersLock.writeLock().unlock();
        }
    }

    /**
     * Unregisters a player from the server.
     *
     * @param player the player
     * @since 1.0
     */
    public void unregisterPlayer(@NonNull JetPlayer player) {
        this.playersLock.writeLock().lock();
        try {
            this.players.remove(player);
        } finally {
            this.playersLock.writeLock().unlock();
        }
    }

    /**
     * Gets a {@linkplain ServerListPing.Favicon server list ping favicon} of the server.
     *
     * @return the icon
     * @since 1.0
     */
    public ServerListPing.@Nullable Favicon serverIcon() {
        return this.serverIcon;
    }
}