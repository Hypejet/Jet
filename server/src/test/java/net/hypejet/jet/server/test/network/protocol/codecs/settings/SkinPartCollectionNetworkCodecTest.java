package net.hypejet.jet.server.test.network.protocol.codecs.settings;

import net.hypejet.jet.entity.player.Player.SkinPart;
import net.hypejet.jet.server.network.protocol.codecs.settings.SkinPartCollectionNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Represents a test of reading and writing of a {@linkplain SkinPartCollectionNetworkCodec skin part collection
 * network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see SkinPartCollectionNetworkCodec
 */
public final class SkinPartCollectionNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(
                SkinPartCollectionNetworkCodec.instance(),
                List.of(SkinPart.CAPE, SkinPart.JACKET, SkinPart.RIGHT_SLEEVE, SkinPart.RIGHT_PANTS)
        );
    }
}