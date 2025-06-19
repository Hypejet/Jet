package net.hypejet.jet.scoreboard.exception;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain RuntimeException a runtime exception} indicating
 * that some {@linkplain net.hypejet.jet.entity.player.Player player} is not a viewer
 * of some {@linkplain net.hypejet.jet.scoreboard.Scoreboard scoreboard}.
 *
 * @since 1.0
 * @see net.hypejet.jet.entity.player.Player
 * @see net.hypejet.jet.scoreboard.Scoreboard
 */
public final class NotViewerException extends RuntimeException {
    /**
     * Constructs the {@linkplain NotViewerException not-viewer exception}.
     *
     * @param message a detail message that the exception should have, {@code null} if none
     * @since 1.0
     */
    public NotViewerException(@Nullable String message) {
        super(message);
    }
}