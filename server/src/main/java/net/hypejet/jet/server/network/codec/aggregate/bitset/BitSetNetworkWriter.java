package net.hypejet.jet.server.network.codec.aggregate.bitset;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * Represents {@linkplain AggregateNetworkWriter an aggregate network writer}, which
 * writes {@linkplain BitSet a bitset}.
 *
 * @since 1.0
 * @author Codestech
 * @see BitSet
 * @see AggregateNetworkWriter
 */
public final class BitSetNetworkWriter extends AggregateNetworkWriter<BitSet> {
    /**
     * An instance of {@linkplain BitSetNetworkWriter a bit set network writer}, which allows lengths
     * up to {@link Integer#MAX_VALUE}.
     *
     * @since 1.0
     */
    public static final BitSetNetworkWriter INSTANCE = new BitSetNetworkWriter(Integer.MAX_VALUE);

    /**
     * Constructs the {@linkplain BitSetNetworkWriter bitset network writer}.
     *
     * @param maxLength a max length that a bitset can have
     * @since 1.0
     */
    public BitSetNetworkWriter(int maxLength) {
        super(maxLength);
    }

    @Override
    protected int length(@NonNull BitSet aggregate) {
        // Get a length of the long array, since we want a length of the long array, not the bit set
        return aggregate.toLongArray().length;
    }

    @Override
    protected void encodeElements(@NonNull BitSet aggregate, @NonNull ByteBuf buf) {
        for (long element : aggregate.toLongArray())
            buf.writeLong(element);
    }
}