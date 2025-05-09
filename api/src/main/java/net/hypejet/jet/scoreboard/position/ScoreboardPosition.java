package net.hypejet.jet.scoreboard.position;

/**
 * Represents position a way of displaying a {@linkplain ??? a scoreboard objective}.
 *
 * <p>The interface is not sealed, since it depends on Minecraft. Adding another class implementing a sealed interface
 * could break switch cases for example.</p>
 *
 * @since 1.0
 * @see ???
 */
public interface ScoreboardPosition {}