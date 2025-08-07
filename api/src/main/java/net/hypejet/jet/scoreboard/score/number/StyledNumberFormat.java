package net.hypejet.jet.scoreboard.score.number;

import net.kyori.adventure.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain NumberFormat a number format} specifying that the score should be formatted
 * with {@linkplain Style a style} specified.
 *
 * @param style the style
 * @since 1.0
 * @see NumberFormat
 */
public record StyledNumberFormat(@NonNull Style style) implements NumberFormat {
    /**
     * Constructs the {@linkplain StyledNumberFormat styled number format}.
     *
     * @param style the styled
     * @since 1.0
     */
    public StyledNumberFormat {
        Objects.requireNonNull(style, "style");
    }
}