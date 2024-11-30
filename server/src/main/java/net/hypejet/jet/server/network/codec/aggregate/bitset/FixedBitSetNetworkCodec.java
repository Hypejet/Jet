package net.hypejet.jet.server.network.codec.aggregate.bitset;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.BitSet;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain BitSet a bitset} using
 * a length specified during creation of the codec.
 *
 * @since 1.0
 * @author Codestech
 * @see BitSet
 * @see NetworkCodec
 */
public final class FixedBitSetNetworkCodec implements NetworkCodec<BitSet> {

    private final int fixedSize;
    private final int fixedByteSize;

    /**
     * Creates {@linkplain FixedBitSetNetworkCodec a fixed bitset network codec}.
     *
     * @param fixedSize the length
     * @since 1.0
     */
    public FixedBitSetNetworkCodec(int fixedSize) {
        this.fixedSize = fixedSize;
        this.fixedByteSize = Math.ceilDiv(fixedSize, 8);
    }

    @Override
    public @NonNull BitSet read(@NonNull ByteBuf buf) {
        return BitSet.valueOf(NetworkUtil.readBytes(buf, this.fixedByteSize));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull BitSet object) {
        if (object.length() > this.fixedSize) {
            throw new IllegalArgumentException(String.format(
                    "The size of the bitset should not be higher than %s", this.fixedSize
            ));
        }
        buf.writeBytes(Arrays.copyOf(object.toByteArray(), this.fixedByteSize));
    }
}