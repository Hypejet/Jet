package net.hypejet.jet.server.test.network.codec.game.world.coordinate;

import net.hypejet.jet.data.model.api.coordinate.Vector;
import net.hypejet.jet.server.network.codec.game.world.coordinate.VectorNetworkCodec;
import net.hypejet.jet.server.test.network.codec.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain VectorNetworkCodec a vector network codec}.
 *
 * @since 1.0
 * @see VectorNetworkCodec
 */
public final class VectorNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(VectorNetworkCodec.INSTANCE, new Vector(1, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }
}