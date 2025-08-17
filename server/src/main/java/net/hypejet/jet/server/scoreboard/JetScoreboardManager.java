package net.hypejet.jet.server.scoreboard;

import net.hypejet.jet.scoreboard.ScoreboardManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@linkplain ScoreboardManager a scoreboard manager}.
 *
 * @since 1.0
 * @see ScoreboardManager
 */
public final class JetScoreboardManager implements ScoreboardManager {

    private final JetScoreboard scoreboard = new JetScoreboard();

    @Override
    public @NonNull JetScoreboard defaultScoreboard() {
        return this.scoreboard;
    }

    @Override
    public @NonNull JetScoreboard createScoreboard() {
        return new JetScoreboard();
    }
}