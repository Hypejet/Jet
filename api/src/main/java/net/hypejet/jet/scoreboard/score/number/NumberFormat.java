package net.hypejet.jet.scoreboard.score.number;

/**
 * Represents a formatting type of scores of {@linkplain ??? a scoreboard objective}.
 *
 * <p>The interface is not sealed, since it depends on Minecraft. Adding another class implementing a sealed interface
 * could break switch cases for example.</p>
 *
 * @since 1.0
 * @see ???
 */
public interface NumberFormat {}