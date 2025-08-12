package net.hypejet.jet.server.test.network.codec.mapper;

import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.test.network.codec.NetworkCodecTestUtil;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * A test of {@linkplain IndexNetworkCodec index network codec} serialization.
 *
 * @since 1.0
 * @see IndexNetworkCodec
 */
public final class IndexNetworkCodecTest {
    @Test
    public void test() {
        byte value = 1;
        Index<Byte, Integer> index = IndexUtil.fromMap(Map.of(
                4, value,
                5, (byte) 2,
                7, (byte) 4
        ));
        NetworkCodecTestUtil.test(new IndexNetworkCodec<>(index, VarIntNetworkCodec.INSTANCE), value);
    }
}