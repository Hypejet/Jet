package net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.AggregateNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an {@linkplain AggregateNetworkCodec aggregate network codec}, which reads and writes a long array.
 *
 * @since 1.0
 * @author Codestech
 * @see AggregateNetworkCodec
 */
public final class LongArrayNetworkCodec extends AggregateNetworkCodec<long[]> {

    private static final LongArrayNetworkCodec INSTANCE = new LongArrayNetworkCodec(Integer.MAX_VALUE);

    private LongArrayNetworkCodec(int maxLength) {
        super(maxLength);
    }

    @Override
    protected int length(long @NonNull [] aggregate) {
        return aggregate.length;
    }

    @Override
    protected void encodeElements(long @NonNull [] aggregate, @NonNull ByteBuf buf) {
        for (long element : aggregate) {
            buf.writeLong(element);
        }
    }

    @Override
    protected long @NonNull [] decodeElements(int length, @NonNull ByteBuf buf) {
        long[] longs = new long[length];
        for (int index = 0; index < length; index++)
            longs[index] = buf.readLong();
        return longs;
    }

    /**
     * Gets an instance of the {@linkplain LongArrayNetworkCodec long array network codec}, which can handle
     * up to {@link Integer#MAX_VALUE} elements.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull LongArrayNetworkCodec instance() {
        return INSTANCE;
    }

    /**
     * Creates a new {@linkplain LongArrayNetworkCodec long array network codec}.
     *
     * @param maxLength a max length of an array that the codec handles
     * @return the codec, {@link #INSTANCE} if the max length specified is {@link Integer#MAX_VALUE}
     * @since 1.0
     */
    public static @NonNull LongArrayNetworkCodec create(int maxLength) {
        if (maxLength == Integer.MAX_VALUE)
            return INSTANCE;
        return new LongArrayNetworkCodec(maxLength);
    }
}