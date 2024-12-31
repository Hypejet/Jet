package net.hypejet.jet.server.test.network.codec.mapper;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.server.network.codec.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.test.network.codec.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain MapperNetworkCodec a mapper network codec}.
 *
 * @since 1.0
 * @see MapperNetworkCodec
 */
public final class MapperNetworkCodecTest {
    @Test
    public void test() {
        byte value = 1;
        Mapper<Byte, Integer> mapper = Mapper.builder(byte.class, int.class)
                .register(value, 4)
                .register((byte) 2, 5)
                .register((byte) 4, 7)
                .build();
        NetworkCodecTestUtil.test(new MapperNetworkCodec<>(mapper, VarIntNetworkCodec.INSTANCE), value);
    }
}