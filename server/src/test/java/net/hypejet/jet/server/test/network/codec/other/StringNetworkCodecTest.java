package net.hypejet.jet.server.test.network.codec.other;

import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.test.network.codec.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain StringNetworkCodec a string network codec}.
 *
 * @since 1.0
 * @see StringNetworkCodec
 */
public final class StringNetworkCodecTest {
    @Test
    public void testUnlimited() {
        NetworkCodecTestUtil.test(StringNetworkCodec.INSTANCE, "some-string");
        NetworkCodecTestUtil.test(StringNetworkCodec.INSTANCE, "another-string");
    }

    @Test
    public void testLimited() {
        StringNetworkCodec codec = StringNetworkCodec.MAX_16_INSTANCE;
        NetworkCodecTestUtil.test(codec, "a-valid-string");
        NetworkCodecTestUtil.testInvalid(codec, "an-invalid-string");
    }
}