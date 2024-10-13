package net.hypejet.jet.server.test.network.protocol.codecs.other;

import net.hypejet.jet.data.model.coordinate.BlockPosition;
import net.hypejet.jet.server.network.protocol.codecs.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain BlockPositionNetworkCodec block position network codec}.
 *
 * @since 1.0
 * @author Codsetech
 * @see BlockPositionNetworkCodec
 */
public final class BlockPositionNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(BlockPositionNetworkCodec.instance(), new BlockPosition(6, 1, 8));
        NetworkCodecTestUtil.test(BlockPositionNetworkCodec.instance(), new BlockPosition(6124, 28, 354));
    }
}