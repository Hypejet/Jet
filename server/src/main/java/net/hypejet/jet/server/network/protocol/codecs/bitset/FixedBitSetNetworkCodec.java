package net.hypejet.jet.server.network.protocol.codecs.bitset;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain BitSet bitset} with
 * a fixed length.
 *
 * @since 1.0
 * @author Codestech
 * @see BitSet
 * @see NetworkCodec
 */
public final class FixedBitSetNetworkCodec implements NetworkCodec<BitSet> {

    private final int fixedByteSize;

    private FixedBitSetNetworkCodec(int fixedSize) {
        this.fixedByteSize = Math.ceilDiv(fixedSize, 8);
    }

    @Override
    public @NonNull BitSet read(@NonNull ByteBuf buf) {
        return BitSet.valueOf(NetworkUtil.readBytes(buf, this.fixedByteSize));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull BitSet object) {
        buf.writeBytes(object.toByteArray());
    }

    /**
     * Creates a {@linkplain FixedBitSetNetworkCodec fixed bitset network codec}.
     *
     * @param fixedSize a fixed size of the bitset
     * @return the network codec
     * @since 1.0
     */
    public static @NonNull FixedBitSetNetworkCodec codec(int fixedSize) {
        return new FixedBitSetNetworkCodec(fixedSize);
    }
}