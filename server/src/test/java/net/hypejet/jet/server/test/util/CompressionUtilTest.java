package net.hypejet.jet.server.test.util;

import net.hypejet.jet.server.util.CompressionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of compression using {@link CompressionUtil compression util}.
 *
 * @since 1.0
 * @see CompressionUtil
 */
public final class CompressionUtilTest {
    @Test
    public void testCompression() {
        byte[] uncompressed = new byte[] { 59, -49, -72, -53 };
        byte[] expected = new byte[] { 120, -100, -77, 62, -65, -29, 52, 0, 5, -104, 2, -114 };
        Assertions.assertArrayEquals(expected, CompressionUtil.compress(uncompressed));
    }

    @Test
    public void testDecompression() {
        byte[] compressed = new byte[] { 120, -100, 99, 100, 100, 102, 100, 6, 0, 0, 28, 0, 10 };
        byte[] expected = new byte[] { 1, 1, 3, 1, 3 };
        Assertions.assertArrayEquals(expected, CompressionUtil.decompress(compressed));
    }

    @Test
    public void testCompressionAndDecompression() {
        byte[] bytes = new byte[] { Byte.MIN_VALUE, 23, 4, 126, 3, -23, 90, -13, Byte.MAX_VALUE, 2, -4, 3, -2, 45, 9 };

        byte[] compressed = CompressionUtil.compress(bytes);
        byte[] decompressed = CompressionUtil.decompress(compressed);

        Assertions.assertArrayEquals(decompressed, bytes);
    }
}