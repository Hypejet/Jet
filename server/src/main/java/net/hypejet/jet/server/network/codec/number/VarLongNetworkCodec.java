package net.hypejet.jet.server.network.codec.number;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes a variable-length long.
 *
 * @since 1.0
 * @see NetworkCodec
 */
public final class VarLongNetworkCodec implements NetworkCodec<Long> {

    private static final byte SEGMENT_BITS = 0x7F;
    private static final short CONTINUE_BIT = 0x80;

    private static final byte MAX_LENGTH = 64;

    /**
     * An instance of the {@linkplain VarLongNetworkCodec variable-length long network codec}.
     *
     * @since 1.0
     */
    public static final VarLongNetworkCodec INSTANCE = new VarLongNetworkCodec();

    private VarLongNetworkCodec() {}

    @Override
    public @NonNull Long read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        long value = 0;

        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = buf.readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;
            if (position >= MAX_LENGTH) {
                throw new IllegalArgumentException(String.format(
                        "Variable-length long is bigger than maximum allowed (%s >= %s)",
                        position, MAX_LENGTH
                ));
            }
        }

        return value;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager, @NonNull Long object) {
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