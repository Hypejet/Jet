package net.hypejet.jet.server.test.network.protocol.codecs.number;

import net.hypejet.jet.server.network.codec.number.VarLongNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain VarLongNetworkCodec a variable-length long network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see VarLongNetworkCodec
 */
public final class VarLongNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(VarLongNetworkCodec.INSTANCE, 235L);
        NetworkCodecTestUtil.test(VarLongNetworkCodec.INSTANCE, Long.MAX_VALUE);
    }
}