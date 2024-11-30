package net.hypejet.jet.server.test.network.protocol.codecs.game.pack;

import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.server.network.codec.game.pack.PackInfoNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain PackInfoNetworkCodec a pack info network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see PackInfoNetworkCodec
 */
public final class PackInfoNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(
                PackInfoNetworkCodec.INSTANCE,
                new PackInfo(Key.key("hypejet", "datapack"), "v1")
        );
    }
}