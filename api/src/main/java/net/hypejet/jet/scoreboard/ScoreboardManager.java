package net.hypejet.jet.scoreboard;

import net.hypejet.jet.MinecraftServer;
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
     * the {@linkplain MinecraftServer server} associated with this {@linkplain ScoreboardManager scoreboard manager}.
     *
     * @return the default scoreboard instance
     * @since 1.0
     */
    @NonNull Scoreboard defaultScoreboard();

    /**
     * Creates {@linkplain Scoreboard a scoreboard} instance.
     *
     * <p>The created scoreboard instance is not registered or tracked by the {@linkplain MinecraftServer server}
     * associated with this {@linkplain ScoreboardManager scoreboard manager}. It remains in memory for as long
     * as it is viewed by any {@linkplain net.hypejet.jet.entity.player.Player player} or tracked
     * by {@linkplain net.hypejet.jet.plugin.Plugin a plugin}.</p>
     *
     * @return the instance
     * @since 1.0
     */
    @NonNull Scoreboard createScoreboard();
}