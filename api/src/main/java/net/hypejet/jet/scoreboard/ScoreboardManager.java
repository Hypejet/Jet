package net.hypejet.jet.scoreboard;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.configuration.ConfigurationStartEvent;
import net.hypejet.jet.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that manages creation of {@linkplain Scoreboard scoreboards}.
 *
 * @since 1.0
 * @see Scoreboard
 */
public interface ScoreboardManager {
    /**
     * Gets {@linkplain Scoreboard a scoreboard} instance that is used as an initial scoreboard (if no other initial
     * scoreboard was specified in {@linkplain ConfigurationStartEvent a configuration start event}) for all
     * {@linkplain Player players} joining the {@linkplain MinecraftServer server} associated
     * with this {@linkplain ScoreboardManager scoreboard manager}.
     *
     * @return the default scoreboard instance
     * @since 1.0
     */
    @NonNull Scoreboard defaultScoreboard();

    /**
     * Creates {@linkplain Scoreboard a scoreboard} instance.
     *
     * <p>The scoreboard is not tracked or registered by default by the server, therefore it exists in memory
     * for as long as any {@linkplain Player player} is a viewer of it, or it is tracked
     * by {@linkplain Plugin plugins}</p>
     *
     * @return the instance
     * @since 1.0
     */
    @NonNull Scoreboard createScoreboard();
}