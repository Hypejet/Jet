package net.hypejet.jet.configuration;

import net.hypejet.jet.util.game.ping.ServerListPing;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a configuration of {@linkplain net.hypejet.jet.MinecraftServer a Minecraft server}.
 *
 * @since 1.0
 */
public interface ServerConfiguration {
    /**
     * Gets a message that should be used as a description of default {@linkplain ServerListPing server list ping}.
     *
     * @return the message
     * @since 1.0
     * @see ServerListPing
     */
    @NonNull Component serverListDescription();

    /**
     * Gets a maximum amount of players, which can be on the server at once.
     *
     * <p>Note that this value by default is used only for server list pinging.</p>
     *
     * @return the amount
     * @since 1.0
     * @see ServerListPing
     */
    int maximumPlayers();

    /**
     * Gets whether the server is in hardcore mode.
     *
     * @return {@code true} if the server is in hardcore mode, {@code false} otherwise
     * @since 1.0
     */
    boolean hardcore();

    /**
     * Gets whether players can only see recipes that they unlocked.
     *
     * <p>This field has no default functionality, it exists only as an information sent to client, however plugins
     * are expected to use this field when implementing functionality depending on it.</p>
     *
     * @return {@code true} if the players can only see recipes that they unlocked, {@code false} otherwise
     * @since 1.0
     */
    boolean showUnlockedRecipesOnly();
}