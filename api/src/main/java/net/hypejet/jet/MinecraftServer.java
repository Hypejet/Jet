package net.hypejet.jet;

import net.hypejet.jet.configuration.ServerConfiguration;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.world.WorldManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

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
     * Gets a brand name of the server.
     *
     * @return the brand name
     * @since 1.0
     */
    @NonNull String brandName();

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

    /**
     * Gets all players, which are connected to the server.
     *
     * <p>Note that all players are returned, even those, which are not in {@linkplain ProtocolState#PLAY play
     * protocol state} yet.</p>
     *
     * @return the players
     * @since 1.0
     */
    @NonNull Collection<Player> players();

    /**
     * Gets all players, which are connected to the server and are in a {@linkplain ProtocolState protocol state}
     * specified.
     *
     * @param state the protocol state
     * @return the players
     * @since 1.0
     */
    @NonNull Collection<Player> players(@NonNull ProtocolState state);

    /**
     * Gets a {@linkplain WorldManager world manager} of the server.
     *
     * @return the world manager
     * @since 1.0
     */
    @NonNull WorldManager worldManager();
}