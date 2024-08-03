
package net.hypejet.jet.server.test.network.protocol.codecs.enums;

import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain EnumVarIntCodec enum variable-length integer codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see EnumVarIntCodec
 */
public final class EnumVarIntCodecTest {
    @Test
    public void test() {
        EnumVarIntCodec<TestEnum> codec = EnumVarIntCodec.builder(TestEnum.class)
                .add(TestEnum.CONST_1, 2)
                .add(TestEnum.CONST_2, 3)
                .add(TestEnum.CONST_3, 1)
                .add(TestEnum.CONST_4, 4)
                .add(TestEnum.CONST_5, 5)
                .add(TestEnum.CONST_6, 6)
                .add(TestEnum.CONST_7, 7)
                .add(TestEnum.CONST_8, 8)
                .build();

        for (TestEnum value : TestEnum.values()) {
            NetworkCodecTestUtil.test(codec, value);
        }
    }

    private enum TestEnum {
        CONST_1,
        CONST_2,
        CONST_3,
        CONST_4,
        CONST_5,
        CONST_6,
        CONST_7,
        CONST_8
    }
}