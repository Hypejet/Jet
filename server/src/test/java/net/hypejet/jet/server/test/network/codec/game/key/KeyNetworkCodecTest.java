package net.hypejet.jet.server.test.network.codec.game.key;

import net.hypejet.jet.server.network.codec.game.key.KeyNetworkCodec;
import net.hypejet.jet.server.test.network.codec.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain KeyNetworkCodec key network codec}.
 *
 * @since 1.0
 * @see KeyNetworkCodec
 */
public final class KeyNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(KeyNetworkCodec.INSTANCE, Key.key("key", "first"));
        NetworkCodecTestUtil.test(KeyNetworkCodec.INSTANCE, Key.key("a-minecraft-key"));
    }
}