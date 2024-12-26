package net.hypejet.jet;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.jet.command.CommandManager;
import net.hypejet.jet.configuration.ServerConfiguration;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.plugin.PluginManager;
import net.hypejet.jet.registry.RegistryManager;
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
     *
     * <p>If the {@linkplain #protocolVersion() protocol version} supports multiple Minecraft versions, the latest one
     * will be returned.</p>
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
     * Creates {@linkplain CollectionAcquisition a collection acquisition} of all players, which are connected to the
     * server.
     *
     * <p>Note that all players are returned, even those, which are not in
     * {@linkplain net.hypejet.jet.network.PlayerConnectionState#PLAY play protocol state} yet.</p>
     *
     * @return the acquisition
     * @since 1.0
     */
    @NonNull CollectionAcquisition<? extends Player, ?> players();

    /**
     * Gets a {@linkplain CommandManager command manager} of the server.
     *
     * @return the command manager
     * @since 1.0
     */
    @NonNull CommandManager commandManager();

    /**
     * Gets a {@linkplain PluginManager plugin manager} of the server.
     *
     * @return the plugin manager
     * @since 1.0
     */
    @NonNull PluginManager pluginManager();

    /**
     * Gets a {@linkplain RegistryManager registry manager} of the server.
     *
     * @return the registry manager
     * @since 1.0
     */
    @NonNull RegistryManager registryManager();
}