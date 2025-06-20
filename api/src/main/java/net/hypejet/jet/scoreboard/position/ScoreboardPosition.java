package net.hypejet.jet.scoreboard.position;

/**
 * Represents a way of displaying
 * {@linkplain net.hypejet.jet.scoreboard.objective.ScoreboardObjective a scoreboard objective}.
 *
 * <p>The interface is not sealed, since it depends on Minecraft. Adding another class implementing a sealed interface
 * could break switch cases for example.</p>
 *
 * @since 1.0
 * @see net.hypejet.jet.scoreboard.objective.ScoreboardObjective
 */
public interface ScoreboardPosition {}