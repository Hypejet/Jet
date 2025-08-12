package net.hypejet.jet.server.test.network.codec.game.registry.feature;

import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.server.network.codec.game.registry.feature.KnownPackNetworkCodec;
import net.hypejet.jet.server.test.network.codec.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * A serialization test of a {@linkplain KnownPackNetworkCodec known pack network-codec}.
 *
 * @since 1.0
 * @see KnownPackNetworkCodec
 */
public final class KnownPackNetworkCodecTest {
    @Test
    public void testCore() {
        NetworkCodecTestUtil.test(
                KnownPackNetworkCodec.INSTANCE,
                new KnownPack("minecraft", "core", "1.21.8")
        );
    }

    @Test
    public void testCustom() {
        NetworkCodecTestUtil.test(
                KnownPackNetworkCodec.INSTANCE,
                new KnownPack("hypejet", "features", "2.0a")
        );
    }
}