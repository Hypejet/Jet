package net.hypejet.jet.util.math;

/**
 * Represents a utility providing mathematical operations.
 *
 * @since 1.0
 * @author Codestech
 */
public final class MathUtil {

    private MathUtil() {}

    /**
     * Clamps a short value specified to fit between the minimum and maximum values specified.
     *
     * @param value the short value
     * @param minimum the minimum
     * @param maximum the maximum
     * @return the clamped value
     * @since 1.0
     */
    public static short clamp(short value, short minimum, short maximum) {
        if (minimum > maximum)
            throw new IllegalArgumentException("The minimum value cannot be higher than the maximum value");
        if (value < minimum)
            return minimum;
        if (value > maximum)
            return maximum;
        return value;
    }
}