package net.hypejet.jet.protocol.packet.server.login.compression;

import net.hypejet.jet.protocol.packet.server.login.ServerLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet}, which is sent to enable a packet compression.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public sealed interface ServerEnableCompressionPacket extends ServerLoginPacket
        permits ServerEnableCompressionPacketImpl {
    /**
     * Gets a maximum size of a packet before it is compressed.
     *
     * @return the size
     * @since 1.0
     */
    int threshold();

    /**
     * Creates a new {@linkplain Builder enable compression packet builder}.
     *
     * @return the builder
     * @since 1.0
     * @see Builder
     */
    static @NonNull Builder builder() {
        return new ServerEnableCompressionPacketImpl.Builder();
    }

    /**
     * Represents a builder for {@linkplain ServerEnableCompressionPacket enable compression packet}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerEnableCompressionPacket
     */
    sealed interface Builder permits ServerEnableCompressionPacketImpl.Builder {
        /**
         * Sets a maximum size of a packet before it is compressed.
         *
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder threshold(int threshold);

        /**
         * Builds the {@linkplain ServerEnableCompressionPacket enable compression packet}.
         *
         * @return the enable compression packet
         * @since 1.0
         */
        @NonNull ServerEnableCompressionPacket build();
    }
}