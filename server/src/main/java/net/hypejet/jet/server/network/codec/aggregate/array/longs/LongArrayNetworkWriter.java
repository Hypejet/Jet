package net.hypejet.jet.server.network.codec.aggregate.array.longs;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain AggregateNetworkWriter an aggregate network writer}, which writes a long array.
 *
 * @since 1.0
 * @see AggregateNetworkWriter
 */
public final class LongArrayNetworkWriter extends AggregateNetworkWriter<long[]> {

    /**
     * An instance of the {@linkplain LongArrayNetworkWriter long array network writer}, which allows lengths
     * up to {@link Integer#MAX_VALUE}.
     *
     * @since 1.0
     */
    public static final LongArrayNetworkWriter INSTANCE = new LongArrayNetworkWriter(Integer.MAX_VALUE);

    /**
     * Constructs the {@linkplain LongArrayNetworkWriter a long array network writer}.
     *
     * @param maxLength a max length that a long array can have
     * @since 1.0
     */
    public LongArrayNetworkWriter(int maxLength) {
        super(maxLength);
    }

    @Override
    protected int length(long @NonNull [] aggregate) {
        return aggregate.length;
    }

    @Override
    protected void encodeElements(long @NonNull [] aggregate, @NonNull ByteBuf buf) {
        for (long element : aggregate)
            buf.writeLong(element);
    }
}