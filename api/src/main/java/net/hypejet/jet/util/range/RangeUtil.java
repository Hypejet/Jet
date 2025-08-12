package net.hypejet.jet.util.range;

/**
 * Represents utilities for math ranges.
 *
 * @since 1.0
 */
public final class RangeUtil {

    private RangeUtil() {}

    /**
     * Ensures that the specified value is within the specified range.
     *
     * @param min the minimum value acceptable in the range, inclusive
     * @param max the maximum value acceptable in the range, inclusive
     * @param value the value to check
     * @throws IllegalArgumentException if the specified value is not within the specified range
     * @since 1.0
     */
    public static void ensureInRange(int min, int max, int value) {
        if (value < min || value > max)
            throw new IllegalArgumentException("The value must be within range [" + min + ";" + max + "]");
    }

    /**
     * Ensures that the specified value is not negative.
     *
     * @param value the value to check
     * @throws IllegalArgumentException if the specified value is negative
     * @since 1.0
     */
    public static void ensureNotNegative(int value) {
        if (value < 0)
            throw new IllegalArgumentException("The value must not be negative");
    }

    /**
     * Ensures that the specified value is not negative.
     *
     * @param value the value to check
     * @throws IllegalArgumentException if the specified value is negative
     * @since 1.0
     */
    public static void ensureNotNegative(float value) {
        if (value < 0)
            throw new IllegalArgumentException("The value must not be negative");
    }
}