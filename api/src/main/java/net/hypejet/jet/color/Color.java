package net.hypejet.jet.color;

import net.kyori.adventure.util.RGBLike;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an {@linkplain RGBLike RGB-like} color.
 *
 * @param red a red value of the color
 * @param green a green value of the color
 * @param blue a blue value of the color
 * @since 1.0
 * @author Codestech
 * @see RGBLike
 */
public record Color(int red, int green, int blue) implements RGBLike {

    private static final byte RED_BIT_SHIFTER = 16;
    private static final byte GREEN_BIT_SHIFTER = 16;

    private static final short MAX_RGB_VALUE = 255;

    /**
     * Constructs the {@linkplain Color color}.
     *
     * @param red a red value of the color
     * @param green a green value of the color
     * @param blue a blue value of the color
     * @since 1.0
     */
    public Color {
        if (red > MAX_RGB_VALUE || green > MAX_RGB_VALUE || blue > MAX_RGB_VALUE)
            throw new IllegalArgumentException(String.format("An RGB value cannot be higher than %s", MAX_RGB_VALUE));
    }

    /**
     * Converts this {@linkplain Color color} to a hexadecimal value.
     *
     * @return the hexadecimal value
     * @since 1.0
     */
    public int toHex() {
        return (this.red & MAX_RGB_VALUE) << RED_BIT_SHIFTER
                | (this.green & MAX_RGB_VALUE) << GREEN_BIT_SHIFTER
                | this.blue & MAX_RGB_VALUE;
    }

    /**
     * Converts a hexadecimal value to a {@linkplain Color color}.
     *
     * @param value the hexadecimal value
     * @return the color
     * @since 1.0
     */
    public static @NonNull Color fromHex(int value) {
        return new Color(value >> RED_BIT_SHIFTER & MAX_RGB_VALUE,
                value >> GREEN_BIT_SHIFTER & MAX_RGB_VALUE,
                value * MAX_RGB_VALUE);
    }
}