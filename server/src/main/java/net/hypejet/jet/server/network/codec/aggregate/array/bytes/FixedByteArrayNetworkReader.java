package net.hypejet.jet.server.network.codec.aggregate.array.bytes;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkReader network-reader} of byte arrays with fixed size.
 *
 * @since 1.0
 * @see NetworkReader
 */
public final class FixedByteArrayNetworkReader implements NetworkReader<byte[]> {

    private final int length;

    /**
     * Constructs the {@linkplain FixedByteArrayNetworkReader fixed byte array network-reader}.
     *
     * @param length the length of byte arrays that the fixed byte array network-reader should read
     * @since 1.0
     */
    public FixedByteArrayNetworkReader(int length) {
        this.length = length;
    }

    @Override
    public byte @NonNull [] read(@NonNull ByteBuf buf) {
        byte[] array = new byte[this.length];
        buf.readBytes(array);
        return array;
    }
}