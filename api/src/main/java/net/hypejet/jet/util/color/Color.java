package net.hypejet.jet.util.color;

import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

/**
 * An RGB color represented by a packed RGB {@code int} value.
 *
 * @param value the packed RGB {@code int} value
 * @since 1.0
 */
public record Color(int value) implements RGBLike {

    private static final byte RED_SHIFT = Byte.SIZE * 2;
    private static final byte GREEN_SHIFT = Byte.SIZE;
    private static final int MAX_RGB_VALUE = 255;

    @Override
    public @Range(from = 0L, to = 255L) int red() {
        return (this.value >> RED_SHIFT) & MAX_RGB_VALUE;
    }

    @Override
    public @Range(from = 0L, to = 255L) int green() {
        return (this.value >> GREEN_SHIFT) & MAX_RGB_VALUE;
    }

    @Override
    public @Range(from = 0L, to = 255L) int blue() {
        return this.value & MAX_RGB_VALUE;
    }

    /**
     * Creates a {@linkplain Color color} from RGB values specified.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     * @return the created color
     * @since 1.0
     */
    public static @NonNull Color fromRGB(int red, int green, int blue) {
        int value = (red << RED_SHIFT) & MAX_RGB_VALUE;
        value |= (green << GREEN_SHIFT) & MAX_RGB_VALUE;
        value |= blue & MAX_RGB_VALUE;
        return new Color(value);
    }
}