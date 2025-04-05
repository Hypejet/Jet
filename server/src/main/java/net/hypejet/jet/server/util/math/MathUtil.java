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

    /**
     * Raises a number specified to a power exponent specified.
     *
     * @param number the number
     * @param exponent the power exponent
     * @return result of the operation
     * @since 1.0
     */
    public static int power(int number, int exponent) {
        if (exponent < 0)
            return 1 / power(number, -exponent);

        int result = 1;
        for (int i = 0; i < exponent; i++)
            result *= number;

        return result;
    }
}