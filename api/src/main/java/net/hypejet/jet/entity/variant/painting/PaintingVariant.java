package net.hypejet.jet.entity.variant.painting;

import net.hypejet.jet.util.range.RangeUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * A variant of Minecraft painting entity.
 *
 * @param width width that paintings of this variant should have
 * @param height height that paintings of this variant should have
 * @param asset the key of the texture that painting of this variant should have
 * @param title the title text that should be applied to the hover text of painting items using this painting variant
 * @param author the author name text that should be applied to the hover text of painting items
 *               using this painting variant
 * @since 1.0
 */
public record PaintingVariant(@Range(from = MIN_SIZE, to = MAX_SIZE) int width,
                              @Range(from = MIN_SIZE, to = MAX_SIZE) int height,
                              @NonNull Key asset, @Nullable Component title,
                              @Nullable Component author) {

    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 16;

    /**
     * Constructs the {@linkplain PaintingVariant painting variant}.
     *
     * @param width width that paintings of this variant should have
     * @param height height that paintings of this variant should have
     * @param asset the key of the texture that painting of this variant should have
     * @param title the title text that should be applied to the hover text of painting items
     *              using this painting variant
     * @param author the author name text that should be applied to the hover text of painting items
     *               using this painting variant
     * @since 1.0
     */
    public PaintingVariant {
        Objects.requireNonNull(asset, "asset");
        RangeUtil.ensureInRange(MIN_SIZE, MAX_SIZE, width);
        RangeUtil.ensureInRange(MIN_SIZE, MAX_SIZE, height);
    }
}