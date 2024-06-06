package net.hypejet.jet.configuration;

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
}