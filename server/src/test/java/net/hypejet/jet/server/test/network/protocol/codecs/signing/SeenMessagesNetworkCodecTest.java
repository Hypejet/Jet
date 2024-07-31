package net.hypejet.jet.server.test.network.protocol.codecs.signing;

import net.hypejet.jet.server.network.protocol.codecs.signing.SeenMessagesNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.hypejet.jet.signing.SeenMessages;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

/**
 * Represents a test of reading and writing of a {@linkplain SeenMessagesNetworkCodec seen messages network codec}.
 *
 * @since 1.0
 * @author Codsestech
 * @see SeenMessagesNetworkCodec
 */
public final class SeenMessagesNetworkCodecTest {
    @Test
    public void test() {
        BitSet set = new BitSet(20);
        set.set(0, 6);
        NetworkCodecTestUtil.test(SeenMessagesNetworkCodec.instance(), new SeenMessages(set.size(), set));
    }
}