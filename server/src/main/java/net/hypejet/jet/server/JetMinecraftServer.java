package net.hypejet.jet.server;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.network.NetworkManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an implementation of {@linkplain MinecraftServer Minecraft server}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class JetMinecraftServer implements MinecraftServer {

    private static final Logger LOGGER  = LoggerFactory.getLogger(MinecraftServer.class);
    private static final int PROTOCOL_VERSION = 766;

    private final EventNode<Object> eventNode = EventNode.create();
    private final JetServerConfiguration configuration;

    private final NetworkManager networkManager;

    /**
     * Constructs the {@linkplain JetMinecraftServer Jet Minecraft server}.
     *
     * @since 1.0
     */
    public JetMinecraftServer() {
        this.configuration = JetServerConfiguration.create();
        this.networkManager = new NetworkManager(this);
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
    public @NonNull JetServerConfiguration configuration() {
        return this.configuration;
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutting down the server...");
        this.networkManager.shutdown();
        LOGGER.info("Successfully shut down the server");
    }
}