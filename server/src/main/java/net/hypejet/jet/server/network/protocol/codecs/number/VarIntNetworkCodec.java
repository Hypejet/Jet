package net.hypejet.jet.server.network.protocol.codecs.number;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a variable-length integer.
 *
 * @since 1.0
 * @author Codestech
 * @see NetworkCodec
 */
public final class VarIntNetworkCodec implements NetworkCodec<Integer> {

    private static final byte SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    private static final int MAX_LENGTH = 32;

    private static final VarIntNetworkCodec INSTANCE = new VarIntNetworkCodec();

    private VarIntNetworkCodec() {}

    @Override
    public @NonNull Integer read(@NonNull ByteBuf buf) {
        int value = 0;
        int position = 0;

        byte currentByte;

        while (true) {
            currentByte = buf.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;
            position += 7;

            if (position >= MAX_LENGTH) {
                throw new IllegalArgumentException("Variable-length integer is bigger than maximum allowed " +
                        "(" + position + " >= " + MAX_LENGTH + ")");
            }
        }

        return value;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Integer object) {
        Objects.requireNonNull(object, "The integer must not be null");
        int value = object;

        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                buf.writeByte(value);
                return;
            }

            buf.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            value >>>= 7;
        }
    }

    /**
     * Gets an instance of the {@linkplain VarIntNetworkCodec variable-length integer network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull VarIntNetworkCodec instance() {
        return INSTANCE;
    }
}