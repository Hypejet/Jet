package net.hypejet.jet.util.color;

import net.kyori.adventure.util.ARGBLike;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

/**
 * An RGBA color represented by a packed ARGB {@code int} value.
 *
 * @param value the packed ARGB {@code int} value
 * @since 1.0
 */
public record ARGBColor(int value) implements ARGBLike {

    private static final int ALPHA_SHIFT = Byte.SIZE * 3;
    private static final int RED_SHIFT = Byte.SIZE * 2;
    private static final int GREEN_SHIFT = Byte.SIZE;
    private static final int MAX_RGB_VALUE = 255;

    @Override
    public @Range(from = 0L, to = 255L) int alpha() {
        return (this.value >>> ALPHA_SHIFT) & MAX_RGB_VALUE;
    }

    @Override
    public @Range(from = 0L, to = 255L) int red() {
        return (this.value >>> RED_SHIFT) & MAX_RGB_VALUE;
    }

    @Override
    public @Range(from = 0L, to = 255L) int green() {
        return (this.value >>> GREEN_SHIFT) & MAX_RGB_VALUE;
    }

    @Override
    public @Range(from = 0L, to = 255L) int blue() {
        return this.value & MAX_RGB_VALUE;
    }

    /**
     * Creates an {@linkplain ARGBColor ARGB color} from the specified ARGB values.
     *
     * @param alpha the alpha value
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     * @return the created ARGB color
     * @since 1.0
     */
    public static @NonNull ARGBColor fromARGB(int alpha, int red, int green, int blue) {
        int value = (alpha << ALPHA_SHIFT) & MAX_RGB_VALUE;
        value |= (red << RED_SHIFT) & MAX_RGB_VALUE;
        value |= (green << GREEN_SHIFT) & MAX_RGB_VALUE;
        value |= blue & MAX_RGB_VALUE;
        return new ARGBColor(value);
    }
}