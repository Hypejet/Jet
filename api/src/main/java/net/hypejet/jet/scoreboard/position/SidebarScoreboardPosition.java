package net.hypejet.jet.scoreboard.position;

import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain ScoreboardPosition a scoreboard position} which specifies that scoreboard scores should be
 * displayed on a sidebar with a visible title, where each score is visible as a pair of the score owner and the score
 * itself.
 *
 * @param color a color of teams whose participants should be able to see the sidebar, {@code null} if the sidebar
 *              should be visible to all players
 * @since 1.0
 * @see ScoreboardPosition
 */
public record SidebarScoreboardPosition(@Nullable NamedTextColor color) implements ScoreboardPosition {}