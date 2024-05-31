package net.hypejet.jet.server.test.util;

import net.hypejet.jet.server.util.CompressionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a test for methods from {@link CompressionUtil compression util}.
 *
 * @since 1.0
 * @author Codestech
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
    public void testRandomBytes() {
        byte[] bytes = new byte[15];
        ThreadLocalRandom.current().nextBytes(bytes);

        byte[] compressed = CompressionUtil.compress(bytes);
        byte[] decompressed = CompressionUtil.decompress(compressed);

        Assertions.assertArrayEquals(decompressed, bytes);
    }
}