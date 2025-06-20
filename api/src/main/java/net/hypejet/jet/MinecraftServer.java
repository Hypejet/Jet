package net.hypejet.jet;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.jet.command.CommandManager;
import net.hypejet.jet.configuration.ServerConfiguration;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.plugin.PluginManager;
import net.hypejet.jet.registry.RegistryManager;
import net.hypejet.jet.scoreboard.ScoreboardManager;
import net.hypejet.jet.world.WorldManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft server.
 *
 * @since 1.0
 */
public interface MinecraftServer {
    /**
     * Gets a root {@linkplain EventNode event node} where all servers events are handled.
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
     * Gets a Minecraft version name of this server.
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
     * Gets {@linkplain ServerConfiguration a server configuration} of the server.
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
     * @return the acquisition
     * @since 1.0
     */
    @NonNull CollectionAcquisition<? extends Player, ?> players();

    /**
     * Gets {@linkplain CommandManager a command manager} of the server.
     *
     * @return the command manager
     * @since 1.0
     */
    @NonNull CommandManager commandManager();

    /**
     * Gets {@linkplain PluginManager a plugin manager} of the server.
     *
     * @return the plugin manager
     * @since 1.0
     */
    @NonNull PluginManager pluginManager();

    /**
     * Gets {@linkplain RegistryManager a registry manager} of the server.
     *
     * @return the registry manager
     * @since 1.0
     */
    @NonNull RegistryManager registryManager();

    /**
     * Gets {@linkplain WorldManager a world manager} of the server.
     *
     * @return the world manager
     * @since 1.0
     */
    @NonNull WorldManager worldManager();

    /**
     * Gets {@linkplain ScoreboardManager a scoreboard manager} of the server.
     *
     * @return the scoreboard manager
     * @since 1.0
     */
    @NonNull ScoreboardManager scoreboardManager();
}