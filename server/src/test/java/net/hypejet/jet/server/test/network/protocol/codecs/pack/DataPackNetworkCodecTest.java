package net.hypejet.jet.server.test.network.protocol.codecs.pack;

import net.hypejet.jet.pack.DataPack;
import net.hypejet.jet.server.network.protocol.codecs.pack.DataPackNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain DataPackNetworkCodec data pack network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see DataPackNetworkCodec
 */
public final class DataPackNetworkCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(DataPackNetworkCodec.instance(), new DataPack(Key.key("hypejet", "datapack"), "v1"));
    }
}