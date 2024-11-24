package net.hypejet.jet.server.test.network.protocol.codecs.enums;

import net.hypejet.jet.server.network.protocol.codecs.enums.EnumByteNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain EnumByteNetworkCodec an enum byte network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see EnumByteNetworkCodec
 */
public final class EnumByteNetworkCodecTest {
    @Test
    public void test() {
        EnumByteNetworkCodec<TestEnum> codec = EnumByteNetworkCodec.builder(TestEnum.class)
                .add(TestEnum.CONST_1, (byte) 2)
                .add(TestEnum.CONST_2, (byte) 3)
                .add(TestEnum.CONST_3, (byte) 1)
                .build();

        for (TestEnum value : TestEnum.values()) {
            NetworkCodecTestUtil.test(codec, value);
        }
    }

    private enum TestEnum {
        CONST_1,
        CONST_2,
        CONST_3
    }
}