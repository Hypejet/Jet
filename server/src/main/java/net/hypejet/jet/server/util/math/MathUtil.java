package net.hypejet.jet.server.util.math;

/**
 * Represents a utility providing mathematical operations.
 *
 * @since 1.0
 */
public final class MathUtil {

    private MathUtil() {}

    /**
     * Gets count of bits that an integer specified uses.
     *
     * @param value the integer
     * @return the count of bits
     * @since 1.0
     */
    public static int bitCount(int value) {
        return Integer.SIZE - Integer.numberOfLeadingZeros(value);
    }
}