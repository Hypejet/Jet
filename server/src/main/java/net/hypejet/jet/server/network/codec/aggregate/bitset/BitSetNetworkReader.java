package net.hypejet.jet.server.network.codec.aggregate.bitset;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.aggregate.AggregateNetworkReader;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * Represents {@linkplain AggregateNetworkReader an aggregate network reader}, which reads
 * {@linkplain BitSet a bitset}.
 *
 * @since 1.0
 * @see BitSet
 * @see AggregateNetworkReader
 */
public final class BitSetNetworkReader extends AggregateNetworkReader<BitSet> {

    /**
     * An instance of the {@linkplain BitSetNetworkReader bit set network reader}, which allows lengths
     * up to {@link Integer#MAX_VALUE}.
     *
     * @since 1.0
     */
    public static final BitSetNetworkReader INSTANCE = new BitSetNetworkReader(Integer.MAX_VALUE);

    /**
     * Constructs the {@linkplain BitSetNetworkReader bitset network reader}.
     *
     * @param maxLength a max length that a bitset can have
     * @since 1.0
     */
    public BitSetNetworkReader(int maxLength) {
        super(maxLength);
    }

    @Override
    protected @NonNull BitSet decodeElements(int length, @NonNull ByteBuf buf,
                                             @NonNull JetRegistryManager registryManager) {
        long[] longs = new long[length];
        for (int index = 0; index < length; index++)
            longs[index] = buf.readLong();
        return BitSet.valueOf(longs);
    }
}