package net.hypejet.jet.configuration;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a configuration for the Jet server.
 *
 * @since 1.0
 * @author Codestech
 */
public interface ServerConfiguration {
    /**
     * Gets a packet length, since which packets are compressed.
     *
     * @return the packet length
     * @since 1.0
     */
    int compressionThreshold();

    /**
     * Gets a message used during disconnection when a player is trying to join with an unsupported version.
     *
     * @return the message
     * @since 1.0
     */
    @NonNull Component unsupportedVersionMessage();

    /**
     * Gets a message used as a description of a {@linkplain net.hypejet.jet.ping.ServerListPing server list ping}.
     *
     * @return the message
     * @since 1.0
     * @see net.hypejet.jet.ping.ServerListPing
     */
    @NonNull Component serverListDescription();

    /**
     * Gets a max amount of players, which can be on the server at once.
     * </p>
     * Note that this value by default is used only by for server list pinging.
     *
     * @return the amount
     * @since 1.0
     * @see net.hypejet.jet.ping.ServerListPing
     */
    int maxPlayers();
}