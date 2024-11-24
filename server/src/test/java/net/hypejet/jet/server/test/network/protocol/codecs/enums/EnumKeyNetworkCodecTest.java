package net.hypejet.jet.server.test.network.protocol.codecs.enums;

import net.hypejet.jet.server.network.protocol.codecs.enums.EnumKeyNetworkCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of {@linkplain EnumKeyNetworkCodec an enum key network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see EnumKeyNetworkCodec
 */
public final class EnumKeyNetworkCodecTest {
    @Test
    public void test() {
        EnumKeyNetworkCodec<TestEnum> codec = EnumKeyNetworkCodec.builder(TestEnum.class)
                .add(TestEnum.CONST_1, Key.key("hypejet", "a-key"))
                .add(TestEnum.CONST_2, Key.key("key"))
                .add(TestEnum.CONST_3, Key.key("jet", "key"))
                .add(TestEnum.CONST_4, Key.key("key", "key"))
                .build();

        for (TestEnum value : TestEnum.values()) {
            NetworkCodecTestUtil.test(codec, value);
        }
    }

    private enum TestEnum {
        CONST_1,
        CONST_2,
        CONST_3,
        CONST_4
    }
}