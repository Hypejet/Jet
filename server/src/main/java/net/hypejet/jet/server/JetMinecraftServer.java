package net.hypejet.jet.server;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.network.NetworkManager;
import net.hypejet.jet.server.util.ServerPingUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * Represents an implementation of {@linkplain MinecraftServer Minecraft server}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class JetMinecraftServer implements MinecraftServer {

    private static final int PROTOCOL_VERSION = 766;
    public static final String MINECRAFT_VERSION = "1.20.6";

    private static final Logger LOGGER  = LoggerFactory.getLogger(MinecraftServer.class);

    private final EventNode<Object> eventNode = EventNode.create();
    private final JetServerConfiguration configuration;

    private final NetworkManager networkManager;

    private final BufferedImage serverIcon;

    /**
     * Constructs the {@linkplain JetMinecraftServer Jet Minecraft server}.
     *
     * @since 1.0
     */
    JetMinecraftServer() {
        this.configuration = JetServerConfiguration.create();
        this.networkManager = new NetworkManager(this);
        this.serverIcon = ServerPingUtil.loadServerIcon(LOGGER);
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
    public @NonNull JetServerConfiguration configuration() {
        return this.configuration;
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutting down the server...");
        this.networkManager.shutdown();
        LOGGER.info("Successfully shut down the server");
    }

    /**
     * Gets an icon of the server, which is used by a {@linkplain net.hypejet.jet.ping.ServerListPing server list ping}.
     *
     * @return the icon
     * @since 1.0
     */
    public @Nullable BufferedImage serverIcon() {
        return this.serverIcon;
    }
}