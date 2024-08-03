package net.hypejet.jet.server.test.network.protocol.codecs.settings;

import net.hypejet.jet.server.network.protocol.codecs.settings.LocaleNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

import java.util.Locale;

/**
 * Represents a test of reading and writing of a {@linkplain LocaleNetworkCodec locale network codec}.
 *
 * @since 1.0
 * @author Codstech
 * @see LocaleNetworkCodec
 */
public final class LocaleNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(LocaleNetworkCodec.instance(), Locale.US);
        NetworkCodecTestUtil.test(LocaleNetworkCodec.instance(), Locale.UK);
        NetworkCodecTestUtil.test(LocaleNetworkCodec.instance(), Locale.CHINA);
    }
}