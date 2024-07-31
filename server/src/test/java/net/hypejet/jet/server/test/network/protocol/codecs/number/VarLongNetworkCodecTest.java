package net.hypejet.jet.server.test.network.protocol.codecs.number;

import net.hypejet.jet.server.network.protocol.codecs.number.VarLongNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain VarLongNetworkCodec variable-length long network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see VarLongNetworkCodec
 */
public final class VarLongNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(VarLongNetworkCodec.instance(), 235L);
        NetworkCodecTestUtil.test(VarLongNetworkCodec.instance(), Long.MAX_VALUE);
    }
}