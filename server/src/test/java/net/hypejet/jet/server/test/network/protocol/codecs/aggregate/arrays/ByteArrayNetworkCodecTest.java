package net.hypejet.jet.server.test.network.protocol.codecs.aggregate.arrays;

import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.ByteArrayNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain ByteArrayNetworkCodec byte array network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see ByteArrayNetworkCodec
 */
public final class ByteArrayNetworkCodecTest {
    @Test
    public void testUnlimitedArray() {
        NetworkCodecTestUtil.test(
                ByteArrayNetworkCodec.instance(),
                new byte[] { 12, 42, 51, 62, 67, 52, 73, 7, 123, 53, 6, 2, 64, 56, 42, 64 },
                Assertions::assertArrayEquals
        );
    }

    @Test
    public void testLimitedArray() {
        ByteArrayNetworkCodec codec = ByteArrayNetworkCodec.create(4);
        NetworkCodecTestUtil.test(codec, new byte[] { 3, 13, 13, 4 }, Assertions::assertArrayEquals);

        NetworkCodecTestUtil.testInvalid(
                codec,
                new byte[] { 52, 76, 75, 34, 64, 13, 52, 64, 12, 1, 2, 34 },
                Assertions::assertArrayEquals
        );
    }
}