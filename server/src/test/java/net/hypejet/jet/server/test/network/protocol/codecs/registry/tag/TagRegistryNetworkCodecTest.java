package net.hypejet.jet.server.test.network.protocol.codecs.registry.tag;

import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.Tag;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.TagRegistry;
import net.hypejet.jet.server.network.protocol.codecs.registry.tag.TagRegistryNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * Represents a test of reading and writing of {@linkplain TagRegistryNetworkCodec tag registry network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see TagRegistryNetworkCodec
 */
public final class TagRegistryNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(
                TagRegistryNetworkCodec.instance(),
                new TagRegistry(Key.key("hypejet", "registry"), Set.of(
                        new Tag(Key.key("a-tag"), new int[] { 214, 421, 432, 623, 71, 726, 832, 532, 751, 732, 5, 23}),
                        new Tag(Key.key("an", "another-tag"), new int[] {})
                ))
        );
    }
}