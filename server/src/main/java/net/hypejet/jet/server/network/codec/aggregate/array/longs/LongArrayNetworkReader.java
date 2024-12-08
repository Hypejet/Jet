package net.hypejet.jet.server.network.codec.aggregate.array.longs;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain AggregateNetworkReader an aggregate network reader}, which reads a long array.
 *
 * @since 1.0
 * @author Codestech
 * @see AggregateNetworkReader
 */
public final class LongArrayNetworkReader extends AggregateNetworkReader<long[]> {

    /**
     * An instance of the {@linkplain LongArrayNetworkReader long array network reader}, which allows lengths
     * up to {@link Integer#MAX_VALUE}.
     *
     * @since 1.0
     */
    public static final LongArrayNetworkReader INSTANCE = new LongArrayNetworkReader(Integer.MAX_VALUE);

    /**
     * Constructs the {@linkplain LongArrayNetworkReader long array network reader}.
     *
     * @param maxLength a max length that a long array can have
     * @since 1.0
     */
    public LongArrayNetworkReader(int maxLength) {
        super(maxLength);
    }

    @Override
    protected long @NonNull [] decodeElements(int length, @NonNull ByteBuf buf) {
        long[] longs = new long[length];
        for (int index = 0; index < longs.length; index++)
            longs[index] = buf.readLong();
        return longs;
    }
}