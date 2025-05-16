package net.hypejet.jet.scoreboard.exception;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

/**
 * Represents {@linkplain NoSuchElementException a no-such-element exception} thrown
 * when {@linkplain net.hypejet.jet.scoreboard.objective.ScoreboardObjective a scoreboard objective} requested
 * does not exist.
 *
 * @since 1.0
 * @see net.hypejet.jet.scoreboard.objective.ScoreboardObjective
 * @see NoSuchElementException
 */
public final class NoSuchObjectiveException extends NoSuchElementException {
    /**
     * Constructs the {@linkplain NoSuchObjectiveException no-such-objective exception}.
     *
     * @param message a detail message that the exception should have, {@code null} if none
     * @since 1.0
     */
    public NoSuchObjectiveException(@Nullable String message) {
        super(message);
    }
}
