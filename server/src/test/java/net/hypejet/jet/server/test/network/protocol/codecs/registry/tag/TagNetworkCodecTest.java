package net.hypejet.jet.server.test.network.protocol.codecs.registry.tag;

import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.registry.tag.TagNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain TagNetworkCodec tag network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see TagNetworkCodec
 */
public final class TagNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(
                TagNetworkCodec.instance(),
                new ServerUpdateTagsConfigurationPacket.Tag(Key.key("an-identifier"), new int[] { 1234, 412, 64, 12 })
        );
    }
}