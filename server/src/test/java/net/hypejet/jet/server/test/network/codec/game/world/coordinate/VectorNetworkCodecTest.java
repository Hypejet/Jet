package net.hypejet.jet.server.test.network.codec.game.world.coordinate;

import net.hypejet.jet.server.network.codec.game.world.coordinate.VectorNetworkCodec;
import net.hypejet.jet.server.test.network.codec.NetworkCodecTestUtil;
import net.hypejet.jet.world.coordinate.Vector;
import org.junit.jupiter.api.Test;

/**
 * A serialization test of a {@linkplain VectorNetworkCodec vector network-codec}.
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