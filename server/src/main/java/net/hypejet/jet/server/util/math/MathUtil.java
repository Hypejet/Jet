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

    /**
     * Gets the greatest {@code int} value that is less or equal to the specified {@code float} value.
     *
     * @param value the {@code float} value that the greatest less-or-equal {@code int} value should be created for
     * @return the created greatest less-or-equal {@code int} value
     * @since 1.0
     */
    public static int floor(float value) {
        int intValue = (int) value;
        return value < intValue ? intValue - 1 : intValue;
    }
}