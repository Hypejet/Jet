package net.hypejet.jet.protocol.packet.server.login.compression;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@linkplain ServerEnableCompressionPacket enable compression packet}.
 *
 * @param threshold a maximum size of packet before it is compressed
 * @since 1.0
 * @author Codestech
 * @see ServerEnableCompressionPacket
 */
record ServerEnableCompressionPacketImpl(int threshold) implements ServerEnableCompressionPacket {
    @Override
    public int getPacketId() {
        return 3;
    }

    @Override
    public void write(@NonNull NetworkBuffer buffer) {
        buffer.writeVarInt(this.threshold);
    }

    /**
     * Represents an implementation of {@linkplain ServerEnableCompressionPacket.Builder enable compression packet
     * builder}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerEnableCompressionPacket.Builder
     */
    static final class Builder implements ServerEnableCompressionPacket.Builder {

        private int threshold;

        @Override
        public ServerEnableCompressionPacket.@NonNull Builder threshold(int threshold) {
            this.threshold = threshold;
            return this;
        }

        @Override
        public @NonNull ServerEnableCompressionPacket build() {
            return new ServerEnableCompressionPacketImpl(this.threshold);
        }
    }
}
