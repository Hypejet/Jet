package net.hypejet.jet.server.network.codec.number;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes a variable-length long.
 *
 * @since 1.0
 * @author Codestech
 * @see NetworkCodec
 */
public final class VarLongNetworkCodec implements NetworkCodec<Long> {

    private static final byte SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    /**
     * An instance of the {@linkplain VarLongNetworkCodec variable-length long network codec}.
     *
     * @since 1.0
     */
    public static final VarLongNetworkCodec INSTANCE = new VarLongNetworkCodec();

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
}