package net.hypejet.jet.server.network.protocol.codecs.number;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a variable-length long.
 *
 * @since 1.0
 * @author Codestech
 * @see NetworkCodec
 */
public final class VarLongNetworkCodec implements NetworkCodec<Long> {

    private static final byte SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    private static final VarLongNetworkCodec INSTANCE = new VarLongNetworkCodec();

    private VarLongNetworkCodec() {}

    @Override
    public @NonNull Long read(@NonNull ByteBuf buf) {
        long value = 0;

        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = buf.readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;
            if (position >= 64) throw new IllegalArgumentException("VarLong is bigger than maximum allowed");
        }

        return value;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Long object) {
        Objects.requireNonNull(object, "The object must not be null");
        long value = object;

        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                buf.writeByte((int) value);
                return;
            }

            buf.writeByte((int) ((value & SEGMENT_BITS) | CONTINUE_BIT));
            value >>>= 7;
        }
    }

    /**
     * Gets an instance of the {@linkplain VarLongNetworkCodec variable-length long network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull VarLongNetworkCodec instance() {
        return INSTANCE;
    }
}