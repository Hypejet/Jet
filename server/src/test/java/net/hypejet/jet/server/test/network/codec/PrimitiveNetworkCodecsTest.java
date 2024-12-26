package net.hypejet.jet.server.test.network.codec;

import net.hypejet.jet.server.network.codec.PrimitiveNetworkCodecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of codecs from {@linkplain PrimitiveNetworkCodecs primitive network
 * codecs}.
 *
 * @since 1.0
 * @see PrimitiveNetworkCodecs
 */
public final class PrimitiveNetworkCodecsTest {
    @Test
    public void testByte() {
        NetworkCodecTestUtil.test(PrimitiveNetworkCodecs.BYTE, (byte) 5, Assertions::assertSame);
    }
}