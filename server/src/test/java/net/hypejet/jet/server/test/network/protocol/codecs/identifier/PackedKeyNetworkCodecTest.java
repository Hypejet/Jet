package net.hypejet.jet.server.test.network.protocol.codecs.identifier;

import net.hypejet.jet.server.network.protocol.codecs.identifier.PackedKeyNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain PackedKeyNetworkCodec packed identifier network
 * codec}.
 *
 * @since 1.0
 * @author Codsetech
 * @see PackedKeyNetworkCodec
 */
public final class PackedKeyNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(PackedKeyNetworkCodec.INSTANCE, Key.key("identifier", "first"));
        NetworkCodecTestUtil.test(PackedKeyNetworkCodec.INSTANCE, Key.key("a-minecraft-identifier"));
    }
}