package net.hypejet.jet.server.network.codec.aggregate.bitset;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * Represents {@linkplain AggregateNetworkWriter an aggregate network writer}, which
 * writes {@linkplain BitSet a bitset}.
 *
 * @since 1.0
 * @see BitSet
 * @see AggregateNetworkWriter
 */
public final class BitSetNetworkWriter extends AggregateNetworkWriter<BitSet> {
    /**
     * An instance of the {@linkplain BitSetNetworkWriter bit set network writer}, which allows for lengths
     * up to {@link Integer#MAX_VALUE} and encodes them.
     *
     * @since 1.0
     */
    public static final BitSetNetworkWriter INSTANCE = new BitSetNetworkWriter(Integer.MAX_VALUE, true);

    /**
     * Constructs the {@linkplain BitSetNetworkWriter bitset network writer}.
     *
     * @param maxLength a max length that a bitset can have
     * @param encodeLength whether the length of bitsets should be encoded
     * @since 1.0
     */
    public BitSetNetworkWriter(int maxLength, boolean encodeLength) {
        super(maxLength, encodeLength);
    }

    @Override
    protected int length(@NonNull BitSet aggregate) {
        // Get a length of the long array, since we want a length of the long array, not the bit set
        return aggregate.toLongArray().length;
    }

    @Override
    protected void encodeElements(@NonNull BitSet aggregate, @NonNull ByteBuf buf,
                                  @NonNull JetRegistryManager registryManager) {
        for (long element : aggregate.toLongArray())
            buf.writeLong(element);
    }
}