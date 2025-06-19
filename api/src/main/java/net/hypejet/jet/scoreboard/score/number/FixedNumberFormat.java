package net.hypejet.jet.scoreboard.score.number;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NumberFormat a number format} specifying that {@linkplain Component a component} specified
 * should be displayed instead of the score number.
 *
 * @param placeholder the component
 * @since 1.0
 */
public record FixedNumberFormat(@NonNull Component placeholder) implements NumberFormat {
    /**
     * Constructs the {@linkplain FixedNumberFormat fixed number format}.
     *
     * @param placeholder the component
     * @since 1.0
     */
    public FixedNumberFormat {
        NullabilityUtil.requireNonNull(placeholder, "placeholder");
    }
}