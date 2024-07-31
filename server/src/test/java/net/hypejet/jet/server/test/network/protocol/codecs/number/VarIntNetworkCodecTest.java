package net.hypejet.jet.server.test.network.protocol.codecs.number;

import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain VarIntNetworkCodec variable-length integer network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see VarIntNetworkCodec
 */
public final class VarIntNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(VarIntNetworkCodec.instance(), 235);
        NetworkCodecTestUtil.test(VarIntNetworkCodec.instance(), Integer.MAX_VALUE);
    }
}