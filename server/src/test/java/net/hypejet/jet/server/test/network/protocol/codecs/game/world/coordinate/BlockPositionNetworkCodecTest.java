package net.hypejet.jet.server.test.network.protocol.codecs.game.world.coordinate;

import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.server.network.codec.game.world.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain BlockPositionNetworkCodec a block position network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see BlockPositionNetworkCodec
 */
public final class BlockPositionNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(
                BlockPositionNetworkCodec.INSTANCE,
                BlockPosition.blockPosition(Integer.MAX_VALUE, Short.MIN_VALUE, -431)
        );
    }
}