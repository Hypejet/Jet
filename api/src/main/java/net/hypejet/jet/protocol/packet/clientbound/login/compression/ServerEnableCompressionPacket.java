package net.hypejet.jet.protocol.packet.clientbound.login.compression;

import net.hypejet.jet.protocol.packet.clientbound.ClientBoundPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link ClientBoundPacket client-bound packet}, which is sent by a server to enable a packet compression.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientBoundPacket
 */
public sealed interface ServerEnableCompressionPacket extends ClientBoundPacket
        permits ServerEnableCompressionPacketImpl {
    /**
     * Gets a maximum size of a packet before it is compressed.
     *
     * @return the size
     * @since 1.0
     */
    int threshold();

    /**
     * Creates a new {@link Builder enable compression packet builder}.
     *
     * @return the builder
     * @since 1.0
     * @see Builder
     */
    static @NonNull Builder builder() {
        return new ServerEnableCompressionPacketImpl.Builder();
    }

    /**
     * Represents a builder for {@link ServerEnableCompressionPacket enable compression packet}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerEnableCompressionPacket
     */
    sealed interface Builder permits ServerEnableCompressionPacketImpl.Builder {
        /**
         * Sets a maximum size of a packet before it is compressed.
         *
         * @return the maximum size
         * @since 1.0
         */
        @NonNull Builder threshold(int threshold);

        /**
         * Builds the {@link ServerEnableCompressionPacket enable compression packet}.
         *
         * @return the enable compression packet
         * @since 1.0
         */
        @NonNull ServerEnableCompressionPacket build();
    }
}