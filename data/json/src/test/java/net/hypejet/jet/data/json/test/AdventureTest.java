package net.hypejet.jet.data.json.test;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.junit.jupiter.api.Test;

/**
 * Represents a GSON conversion test of adventure library objects.
 *
 * @since 1.0
 */
final class AdventureTest {
    @Test
    void testKeyWithDefaultNamespace() {
        TestUtil.test(Key.key("glowstone"));
    }

    @Test
    void testKeyWithCustomNamespace() {
        TestUtil.test(Key.key("hypejet", "jet"));
    }

    @Test
    void testCompoundBinaryTagHolder() {
        TestUtil.test(BinaryTagHolder.binaryTagHolder("{test:\"This is a test!\",number:45,float:5352.5f}"));
    }

    @Test
    void testStringBinaryTagHolder() {
        TestUtil.test(BinaryTagHolder.binaryTagHolder("Another test!"));
    }

    @Test
    public void testByteBinaryTagHolder() {
        TestUtil.test(BinaryTagHolder.binaryTagHolder("32b"));
    }

    @Test
    public void testNegativeDoubleBinaryTagHolder() {
        TestUtil.test(BinaryTagHolder.binaryTagHolder("-3.1415926"));
    }
}