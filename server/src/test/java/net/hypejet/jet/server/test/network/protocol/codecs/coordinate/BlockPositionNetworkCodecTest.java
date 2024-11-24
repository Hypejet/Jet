package net.hypejet.jet.server.test.network.protocol.codecs.coordinate;

import net.hypejet.jet.data.model.api.coordinate.BlockPosition;
import net.hypejet.jet.server.network.protocol.codecs.coordinate.BlockPositionNetworkCodec;
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
                new BlockPosition(2, 51352, -61)
        );
    }
}