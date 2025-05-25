package net.hypejet.jet.scoreboard;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that manages creation of {@linkplain Scoreboard scoreboards}.
 *
 * @since 1.0
 * @see Scoreboard
 */
public interface ScoreboardManager {
    /**
     * Gets {@linkplain Scoreboard a scoreboard} instance that is used by default
     * for all {@linkplain net.hypejet.jet.entity.player.Player players} joining
     * {@linkplain net.hypejet.jet.MinecraftServer a server} associated
     * with this {@linkplain ScoreboardManager scoreboard manager}.
     *
     * @return the default scoreboard instance
     * @since 1.0
     */
    @NonNull Scoreboard defaultScoreboard();

    /**
     * Creates {@linkplain Scoreboard a scoreboard} instance.
     *
     * @return the instance
     * @since 1.0
     */
    @NonNull Scoreboard createScoreboard();
}