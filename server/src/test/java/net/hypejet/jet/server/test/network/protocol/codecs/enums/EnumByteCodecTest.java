package net.hypejet.jet.server.test.network.protocol.codecs.enums;

import net.hypejet.jet.server.network.protocol.codecs.enums.EnumByteCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain EnumByteCodec enum byte codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see EnumByteCodec
 */
public final class EnumByteCodecTest {
    @Test
    public void test() {
        EnumByteCodec<TestEnum> codec = EnumByteCodec.builder(TestEnum.class)
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