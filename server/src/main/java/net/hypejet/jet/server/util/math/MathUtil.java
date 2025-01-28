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
        if (value == 0)
            return 0; // Logarithmic numbers must be higher than 0
        return ceilLog2(Math.abs(value)) + 1;
    }

    /**
     * Calculates an integer-expressed logarithm with base of {@code 2} with a logarithmic number specified. If the
     * result is not an integer, the smallest integer that is bigger than the result is returned.
     *
     * @param value the logarithmic number
     * @return a result of the operation
     * @since 1.0
     */
    public static int ceilLog2(int value) {
        int result = log2(value);
        if (isPowerOf2(value))
            return result;
        return result + 1; // The logarithm result is rounded to an integer, so we need to simply sum it with 1
    }

    /**
     * Calculates an integer-expressed logarithm with base of {@code 2} with a logarithmic number specified.
     *
     * @param value the logarithmic number, which is expressed as an integer
     * @return the result of the logarithm, which is expressed as an integer
     * @since 1.0
     * @throws IllegalArgumentException if the logarithmic number is lower or equal than {@code 0}
     */
    public static int log2(int value) {
        if (value <= 0)
            throw new IllegalArgumentException("A logarithmic number must be higher than 0");
        return (Integer.SIZE - 1) - Integer.numberOfLeadingZeros(value);
    }

    /**
     * Gets whether an integer specified is a power of {@code 2}.
     *
     * @param value the integer
     * @return {@code true} if the integer specified is a power of {@code 2}, {@code false} otherwise
     * @since 1.0
     */
    public static boolean isPowerOf2(int value) {
        return (value & (value - 1)) == 0;
    }
}