package net.hypejet.jet.scoreboard.position;

import java.util.Objects;

/**
 * Represents {@linkplain ScoreboardPosition scoreboard position} which specifies that scoreboard scores should be
 * visible on player list entries of corresponding players.
 *
 * @since 1.0
 * @see ScoreboardPosition
 */
public final class PlayerListScoreboardPosition implements ScoreboardPosition {
    /**
     * An instance of the {@linkplain PlayerListScoreboardPosition player list scoreboard position}.
     *
     * @since 1.0
     */
    public static final PlayerListScoreboardPosition INSTANCE = new PlayerListScoreboardPosition();

    private PlayerListScoreboardPosition() {}

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlayerListScoreboardPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public String toString() {
        return "PlayerListScoreboardPosition{}";
    }
}