package net.hypejet.jet.server.test.scoreboard;

import net.hypejet.jet.scoreboard.objective.ScoreboardObjective;
import net.hypejet.jet.scoreboard.score.Score;
import net.hypejet.jet.scoreboard.score.number.BlankNumberFormat;
import net.hypejet.jet.scoreboard.score.render.RenderType;
import net.hypejet.jet.server.scoreboard.JetScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents test of {@linkplain JetScoreboard a scoreboard}.
 *
 * @since 1.0
 * @see JetScoreboard
 */
public final class JetScoreboardTest {
    @Test
    public void testObjectives() {
        String name = "test-scoreboard-objective";

        JetScoreboard scoreboard = new JetScoreboard();
        Assertions.assertNull(scoreboard.getObjective(name));

        ScoreboardObjective objective = new ScoreboardObjective(
                Component.text("Text scoreboard objective", NamedTextColor.RED),
                RenderType.INTEGER, BlankNumberFormat.INSTANCE
        );

        scoreboard.setObjective(name, objective);
        Assertions.assertSame(scoreboard.getObjective(name), objective);

        objective = new ScoreboardObjective(objective.displayName(), RenderType.HEARTS, objective.numberFormat());
        scoreboard.setObjective(name, objective);
        Assertions.assertSame(scoreboard.getObjective(name), objective);

        scoreboard.setObjective(name, null);
        Assertions.assertNull(scoreboard.getObjective(name));
    }

    @Test
    public void testScores() {
        String objective = "example-objective";

        String firstOwner = "owner-1";
        String secondOwner = "second-owner";

        JetScoreboard scoreboard = new JetScoreboard();
        scoreboard.setObjective(objective, new ScoreboardObjective(Component.empty(), RenderType.HEARTS, null));

        Assertions.assertNull(scoreboard.getScore(firstOwner, objective));
        Assertions.assertNull(scoreboard.getScore(secondOwner, objective));

        Score firstOwnerScore = new Score(20, null, null);
        scoreboard.setScore(firstOwner, objective, firstOwnerScore);
        Assertions.assertSame(firstOwnerScore, scoreboard.getScore(firstOwner, objective));
        Assertions.assertNull(scoreboard.getScore(secondOwner, objective));

        Score secondOwnerScore = new Score(-50, null, null);
        scoreboard.setScore(secondOwner, objective, secondOwnerScore);
        Assertions.assertSame(firstOwnerScore, scoreboard.getScore(firstOwner, objective));
        Assertions.assertSame(secondOwnerScore, scoreboard.getScore(secondOwner, objective));

        scoreboard.setScore(firstOwner, objective, null);
        Assertions.assertNull(scoreboard.getScore(firstOwner, objective));
        Assertions.assertSame(secondOwnerScore, scoreboard.getScore(secondOwner, objective));

        scoreboard.setScore(secondOwner, objective, null);
        Assertions.assertNull(scoreboard.getScore(firstOwner, objective));
        Assertions.assertNull(scoreboard.getScore(secondOwner, objective));
    }
}