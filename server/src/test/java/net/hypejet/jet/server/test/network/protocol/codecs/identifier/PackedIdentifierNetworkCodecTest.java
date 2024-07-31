package net.hypejet.jet.server.test.network.protocol.codecs.identifier;

import net.hypejet.jet.server.network.protocol.codecs.identifier.PackedIdentifierNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain PackedIdentifierNetworkCodec packed identifier network
 * codec}.
 *
 * @since 1.0
 * @author Codsetech
 * @see PackedIdentifierNetworkCodec
 */
public final class PackedIdentifierNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(PackedIdentifierNetworkCodec.instance(), Key.key("identifier", "first"));
        NetworkCodecTestUtil.test(PackedIdentifierNetworkCodec.instance(), Key.key("a-minecraft-identifier"));
    }
}