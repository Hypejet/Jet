package net.hypejet.jet.server.test.network.protocol.codecs.identifier;

import net.hypejet.jet.server.network.protocol.codecs.identifier.IdentifierNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain IdentifierNetworkCodec identifier network codec}.
 *
 * @since 1.0
 * @author Codsetech
 * @see IdentifierNetworkCodec
 */
public final class IdentifierNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(IdentifierNetworkCodec.instance(), Key.key("identifier", "first"));
        NetworkCodecTestUtil.test(IdentifierNetworkCodec.instance(), Key.key("a-minecraft-identifier"));
    }
}