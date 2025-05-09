package net.hypejet.jet.scoreboard.position;

import java.util.Objects;

/**
 * Represents {@linkplain ScoreboardPosition a scoreboard position} which specifies that scoreboard scores should be
 * displayed below name tags located above heads of their owners.
 *
 * @since 1.0
 * @see ScoreboardPosition
 */
public final class BelowNameScoreboardPosition implements ScoreboardPosition {
    /**
     * An instance of the {@linkplain BelowNameScoreboardPosition below-name scoreboard position}.
     *
     * @since 1.0
     */
    public static final BelowNameScoreboardPosition INSTANCE = new BelowNameScoreboardPosition();

    private BelowNameScoreboardPosition() {}

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BelowNameScoreboardPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public String toString() {
        return "BelowNameScoreboardPosition{}";
    }
}