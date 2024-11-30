package net.hypejet.jet.server.test.network.protocol.codecs.game.key;

import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain PackedKeyNetworkCodec a packed key network codec}.
 *
 * @since 1.0
 * @author Codsetech
 * @see PackedKeyNetworkCodec
 */
public final class PackedKeyNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(PackedKeyNetworkCodec.INSTANCE, Key.key("key", "first"));
        NetworkCodecTestUtil.test(PackedKeyNetworkCodec.INSTANCE, Key.key("a-minecraft-key"));
    }
}