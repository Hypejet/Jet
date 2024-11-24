package net.hypejet.jet.server.test.network.protocol.codecs.signing;

import net.hypejet.jet.server.network.protocol.codecs.signing.SignedArgumentNetworkReader;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.hypejet.jet.signing.SignedArgument;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a test of reading and writing of a {@linkplain SignedArgumentNetworkReader signed argument network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see SignedArgumentNetworkReader
 */
public final class SignedArgumentNetworkReaderTest {
    @Test
    public void test() {
        byte[] signature = new byte[256];
        ThreadLocalRandom.current().nextBytes(signature);
        NetworkCodecTestUtil.test(SignedArgumentNetworkReader.instance(), new SignedArgument("a-name", signature));
    }
}