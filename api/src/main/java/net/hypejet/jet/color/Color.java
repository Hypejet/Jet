package net.hypejet.jet.color;

import net.kyori.adventure.util.RGBLike;

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
}