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
     * Gets a message used as a description of server list ping.
     *
     * @return the message
     * @since 1.0
     */
    @NonNull Component serverListDescription();
}