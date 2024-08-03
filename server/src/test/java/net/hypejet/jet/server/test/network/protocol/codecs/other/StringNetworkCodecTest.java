package net.hypejet.jet.server.test.network.protocol.codecs.other;

import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain StringNetworkCodec string network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see StringNetworkCodec
 */
public final class StringNetworkCodecTest {
    @Test
    public void testUnlimited() {
        NetworkCodecTestUtil.test(StringNetworkCodec.instance(), "some-string");
        NetworkCodecTestUtil.test(StringNetworkCodec.instance(), "another-string");
    }

    @Test
    public void testLimited() {
        StringNetworkCodec codec = StringNetworkCodec.create(16);
        NetworkCodecTestUtil.test(codec, "a-valid-string");
        NetworkCodecTestUtil.testInvalid(codec, "an-invalid-string");
    }
}