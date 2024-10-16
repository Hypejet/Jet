package net.hypejet.jet.server.test.network.protocol.codecs.pack;

import net.hypejet.jet.data.model.pack.DataPack;
import net.hypejet.jet.data.model.pack.info.PackInfo;
import net.hypejet.jet.server.network.protocol.codecs.pack.PackInfoNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain PackInfoNetworkCodec pack info network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see PackInfoNetworkCodec
 */
public final class PackInfoNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(PackInfoNetworkCodec.instance(), new PackInfo(Key.key("hypejet", "datapack"), "v1"));
    }
}