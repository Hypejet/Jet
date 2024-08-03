package net.hypejet.jet.server.test.network.protocol.codecs.other;

import net.hypejet.jet.server.network.protocol.codecs.other.BinaryTagCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain BinaryTagCodec binary tag codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see BinaryTagCodec
 */
public final class BinaryTagCodecTest {
    @Test
    public void testString() {
        StringBinaryTag tag = StringBinaryTag.stringBinaryTag("some-very-very-very-long-string");
        NetworkCodecTestUtil.test(BinaryTagCodec.instance(), tag);
    }

    @Test
    public void testCompound() {
        CompoundBinaryTag tag = CompoundBinaryTag.builder()
                .putDouble("a-number", Math.PI)
                .putBoolean("bool", true)
                .putIntArray("an-integer-array", new int[] { 124, 61, 65213, 6346, 6432 })
                .putString("a-text", "the text")
                .build();
        NetworkCodecTestUtil.test(BinaryTagCodec.instance(), tag);
    }
}