package net.hypejet.jet.server.test.network.codec.number;

import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.test.network.codec.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain VarIntNetworkCodec a variable-length integer network codec}.
 *
 * @since 1.0
 * @see VarIntNetworkCodec
 */
public final class VarIntNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(VarIntNetworkCodec.INSTANCE, Integer.MIN_VALUE);
        NetworkCodecTestUtil.test(VarIntNetworkCodec.INSTANCE, -654254);
        NetworkCodecTestUtil.test(VarIntNetworkCodec.INSTANCE, 235);
        NetworkCodecTestUtil.test(VarIntNetworkCodec.INSTANCE, Integer.MAX_VALUE);
    }
}