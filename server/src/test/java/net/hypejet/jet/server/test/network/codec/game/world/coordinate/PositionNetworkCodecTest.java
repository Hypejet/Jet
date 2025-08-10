package net.hypejet.jet.server.test.network.codec.game.world.coordinate;

import net.hypejet.jet.server.network.codec.game.world.coordinate.PositionNetworkCodec;
import net.hypejet.jet.server.test.network.codec.NetworkCodecTestUtil;
import net.hypejet.jet.world.coordinate.Position;
import org.junit.jupiter.api.Test;

/**
 * A serialization test of a {@linkplain PositionNetworkCodec position network-codec}.
 *
 * @since 1.0
 * @see PositionNetworkCodec
 */
public final class PositionNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(PositionNetworkCodec.INSTANCE, new Position(-3, 4, 324, 12, -1));
    }
}