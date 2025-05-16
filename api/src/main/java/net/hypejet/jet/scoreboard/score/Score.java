package net.hypejet.jet.scoreboard.score;

import net.hypejet.jet.scoreboard.score.number.NumberFormat;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a score data of some entity in some
 * {@linkplain net.hypejet.jet.scoreboard.objective.ScoreboardObjective scoreboard objective}.
 *
 * @param score a score of the entity
 * @param displayName a text that should be displayed instead of a name of the entity in case when score owner name
 *                    is displayed, {@code null} if the entity name should be used instead
 * @param numberFormat a number format that should be applied to the score, {@code null} if default formatting
 *                     should be used
 * @see net.hypejet.jet.scoreboard.objective.ScoreboardObjective
 */
public record Score(int score, @Nullable Component displayName, @Nullable NumberFormat numberFormat) {}