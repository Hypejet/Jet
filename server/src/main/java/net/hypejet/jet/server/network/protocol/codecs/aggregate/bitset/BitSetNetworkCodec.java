package net.hypejet.jet.server.network.protocol.codecs.aggregate.bitset;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.AggregateNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * Represents a {@linkplain AggregateNetworkCodec aggregate network codec}, which reads and writes
 * a {@linkplain BitSet bitset}.
 *
 * @since 1.0
 * @author Codestech
 * @see BitSet
 * @see AggregateNetworkCodec
 */
public final class BitSetNetworkCodec extends AggregateNetworkCodec<BitSet> {

    private static final BitSetNetworkCodec INSTANCE = new BitSetNetworkCodec(Integer.MAX_VALUE);

    /**
     * Constructs the {@linkplain BitSetNetworkCodec bitset network codec}.
     *
     * @param maxLength a max length that a bitset can have
     * @since 1.0
     */
    private BitSetNetworkCodec(int maxLength) {
        super(maxLength);
    }

    @Override
    protected int length(@NonNull BitSet aggregate) {
        return aggregate.toLongArray().length;
    }

    @Override
    protected void encodeElements(@NonNull BitSet aggregate, @NonNull ByteBuf buf) {
        for (long element : aggregate.toLongArray()) {
            buf.writeLong(element);
        }
    }

    @Override
    protected @NonNull BitSet decodeElements(int length, @NonNull ByteBuf buf) {
        long[] longs = new long[length];
        for (int index = 0; index < length; index++)
            longs[index] = buf.readLong();
        return BitSet.valueOf(longs);
    }

    /**
     * Creates a {@linkplain BitSetNetworkCodec bitset network codec}.
     *
     * @param maxLength a maximum length of bitsets, which the codec should read and write
     * @return the codec, {@link #INSTANCE} is returned if the maximum length is {@link Integer#MAX_VALUE}
     * @since 1.0
     */
    public static @NonNull BitSetNetworkCodec create(int maxLength) {
        if (maxLength == Integer.MAX_VALUE) return INSTANCE;
        return new BitSetNetworkCodec(maxLength);
    }

    /**
     * Gets an instance of the {@linkplain BitSetNetworkCodec bitset network codec}, whose maximum length
     * is {@link Integer#MAX_VALUE}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BitSetNetworkCodec instance() {
        return INSTANCE;
    }
}