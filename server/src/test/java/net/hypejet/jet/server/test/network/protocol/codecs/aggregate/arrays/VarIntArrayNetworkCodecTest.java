package net.hypejet.jet.server.test.network.protocol.codecs.aggregate.arrays;

import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.VarIntArrayNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain VarIntArrayNetworkCodec variable-length integer array
 * network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see VarIntArrayNetworkCodec
 */
public final class VarIntArrayNetworkCodecTest {
    @Test
    public void testUnlimitedArray() {
        NetworkCodecTestUtil.test(
                VarIntArrayNetworkCodec.instance(),
                new int[] { 12, 42, 12351, 62, 67, 5212, 73, 7, 125323, 53, 432532116, 2, 64, 56, 42, 64321244 },
                Assertions::assertArrayEquals
        );
    }

    @Test
    public void testLimitedArray() {
        VarIntArrayNetworkCodec codec = VarIntArrayNetworkCodec.create(4);
        NetworkCodecTestUtil.test(codec, new int[]{314133, 11343, 13, 43114}, Assertions::assertArrayEquals);

        NetworkCodecTestUtil.testInvalid(
                codec,
                new int[]{13452, 76, 75, 34134, 64, 13, 52, 64, 12, 1, 21231231, 345245612},
                Assertions::assertArrayEquals
        );
    }
}