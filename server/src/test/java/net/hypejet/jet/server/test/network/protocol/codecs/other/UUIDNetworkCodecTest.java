package net.hypejet.jet.server.test.network.protocol.codecs.other;

import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * Represents a test of reading and writing of a {@linkplain UUIDNetworkCodec unique identifier network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see UUIDNetworkCodec
 */
public final class UUIDNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(UUIDNetworkCodec.instance(), UUID.randomUUID());
    }
}