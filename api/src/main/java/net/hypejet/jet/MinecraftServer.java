package net.hypejet.jet;

import net.hypejet.jet.configuration.ServerConfiguration;
import net.hypejet.jet.event.node.EventNode;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an interface for managing a Minecraft server.
 *
 * @since 1.0
 * @author Codestech
 */
public interface MinecraftServer {
    /**
     * Gets a main {@linkplain EventNode event node}, on which all events are called.
     *
     * @return the event node
     * @since 1.0
     */
    @NonNull EventNode<Object> eventNode();

    /**
     * Gets a version of Minecraft protocol that this server supports.
     *
     * @return the version
     * @since 1.0
     */
    int protocolVersion();

    /**
     * Gets a Minecraft version name that this server supports.
     * </p>
     * If the {@linkplain #protocolVersion() protocol version} supports multiple Minecraft versions, the latest one
     * will be returned.
     *
     * @return the Minecraft version name
     * @since 1.0
     */
    @NonNull String minecraftVersion();

    /**
     * Gets a {@linkplain ServerConfiguration server configuration} of this server.
     *
     * @return the configuration
     * @since 1.0
     */
    @NonNull ServerConfiguration configuration();

    /**
     * Shuts down the server.
     *
     * @since 1.0
     */
    void shutdown();
}