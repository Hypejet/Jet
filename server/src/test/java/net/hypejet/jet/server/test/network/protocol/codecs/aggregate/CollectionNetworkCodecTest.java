package net.hypejet.jet.server.test.network.protocol.codecs.aggregate;

import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Represents a test of reading and writing of a {@linkplain CollectionNetworkCodec collection network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see CollectionNetworkCodec
 */
public final class CollectionNetworkCodecTest {
    @Test
    public void testUnlimitedCollection() {
        CollectionNetworkCodec<String> codec = CollectionNetworkCodec.create(StringNetworkCodec.instance());
        NetworkCodecTestUtil.test(codec, List.of("some-string", "another-string", "third-string"));
    }

    @Test
    public void testLimitedCollection() {
        CollectionNetworkCodec<String> codec = CollectionNetworkCodec.create(StringNetworkCodec.instance(), 2);
        NetworkCodecTestUtil.test(codec, List.of("some-string", "another-string"));
        NetworkCodecTestUtil.testInvalid(codec, List.of("some-string", "another-string", "third-string"));
    }
}