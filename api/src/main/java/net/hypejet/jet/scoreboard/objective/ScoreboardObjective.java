package net.hypejet.jet.scoreboard.objective;

import net.hypejet.jet.scoreboard.score.number.NumberFormat;
import net.hypejet.jet.scoreboard.score.render.RenderType;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents data of a Minecraft scoreboard objective.
 *
 * @param displayName a display name that the scoreboard objective should have
 * @param renderType a way how score numbers of the scoreboard objective should be displayed
 * @param numberFormat a formatting type that should be applied to score numbers of the scoreboard objective,
 *                     {@code null} if default formatting should be used instead
 * @since 1.0
 */
public record ScoreboardObjective(@NonNull Component displayName, @NonNull RenderType renderType,
                                  @Nullable NumberFormat numberFormat) {
    /**
     * Constructs the {@linkplain ScoreboardObjective scoreboard objective}.
     *
     * @param displayName a display name that the scoreboard objective should have
     * @param renderType a way how score numbers of the scoreboard objective should be displayed
     * @param numberFormat a formatting type that should be applied to score numbers of the scoreboard objective,
     *                     {@code null} if default formatting should be used instead
     * @since 1.0
     */
    public ScoreboardObjective {
        Objects.requireNonNull(displayName, "display name");
        Objects.requireNonNull(renderType, "render type");
    }
}