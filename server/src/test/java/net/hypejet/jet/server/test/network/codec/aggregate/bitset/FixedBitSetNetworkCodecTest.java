package net.hypejet.jet.server.test.network.codec.aggregate.bitset;

import net.hypejet.jet.server.network.codec.aggregate.bitset.FixedBitSetNetworkCodec;
import net.hypejet.jet.server.test.network.codec.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

/**
 * Represents a test of reading and writing of {@linkplain FixedBitSetNetworkCodec a fixed bitset network codec}.
 *
 * @since 1.0
 * @see FixedBitSetNetworkCodec
 */
public final class FixedBitSetNetworkCodecTest {
    @Test
    public void test() {
        FixedBitSetNetworkCodec codec = new FixedBitSetNetworkCodec(20);

        BitSet validBitSet = new BitSet(20);
        validBitSet.set(0);
        validBitSet.set(4);
        validBitSet.set(12);

        NetworkCodecTestUtil.test(codec, validBitSet);

        BitSet invalidBitSet = new BitSet(40);
        invalidBitSet.set(0);
        invalidBitSet.set(4);
        invalidBitSet.set(12);
        invalidBitSet.set(32);

        NetworkCodecTestUtil.testInvalid(codec, invalidBitSet);
    }
}