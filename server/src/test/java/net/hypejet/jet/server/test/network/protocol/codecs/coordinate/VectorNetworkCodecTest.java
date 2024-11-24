package net.hypejet.jet.server.test.network.protocol.codecs.coordinate;

import net.hypejet.jet.data.model.api.coordinate.Vector;
import net.hypejet.jet.server.network.protocol.codecs.coordinate.VectorNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain VectorNetworkCodec a vector network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see VectorNetworkCodec
 */
public final class VectorNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(VectorNetworkCodec.INSTANCE, new Vector(1, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }
}