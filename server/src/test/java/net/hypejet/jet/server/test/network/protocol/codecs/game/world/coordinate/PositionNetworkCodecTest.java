package net.hypejet.jet.server.test.network.protocol.codecs.game.world.coordinate;

import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.server.network.codec.game.world.coordinate.PositionNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain PositionNetworkCodec a position network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see PositionNetworkCodec
 */
public final class PositionNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(PositionNetworkCodec.INSTANCE, new Position(-3, 4, 324, 12, -1));
    }
}