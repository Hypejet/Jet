package net.hypejet.jet.server.network.codec.aggregate.array.longs;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An {@linkplain AggregateNetworkWriter aggregate network-writer} writing a long array.
 *
 * @since 1.0
 * @see AggregateNetworkWriter
 */
public final class LongArrayNetworkWriter extends AggregateNetworkWriter<long[]> {

    /**
     * An instance of the {@linkplain LongArrayNetworkWriter long array network-writer}
     * allowing lengths up to {@link Integer#MAX_VALUE} and encoding them.
     *
     * @since 1.0
     */
    public static final LongArrayNetworkWriter INSTANCE = new LongArrayNetworkWriter(Integer.MAX_VALUE, true);

    /**
     * An instance of the {@linkplain LongArrayNetworkWriter long array network-writer}
     * allowing lengths up to {@link Integer#MAX_VALUE} and not encoding them.
     *
     * @since 1.0
     */
    public static final LongArrayNetworkWriter FIXED_INSTANCE = new LongArrayNetworkWriter(Integer.MAX_VALUE, false);

    /**
     * Constructs the {@linkplain LongArrayNetworkWriter a long array network writer}.
     *
     * @param maxLength a max length that a long array can have
     * @param encodeLength whether length of long arrays should be encoded
     * @since 1.0
     */
    public LongArrayNetworkWriter(int maxLength, boolean encodeLength) {
        super(maxLength, encodeLength);
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