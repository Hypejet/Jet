package net.hypejet.jet.event.events.lifecycle;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.scoreboard.ScoreboardManager;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An event called when the {@linkplain ScoreboardManager scoreboard manager}
 * of the {@linkplain MinecraftServer server} has been initialized.
 *
 * @param scoreboardManager the initialized scoreboard manager
 * @since 1.0
 * @see ScoreboardManager
 * @see MinecraftServer
 */
public record ScoreboardManagerLoadEvent(@NonNull ScoreboardManager scoreboardManager) {
    /**
     * Constructs the {@linkplain ScoreboardManagerLoadEvent scoreboard manager load event}.
     *
     * @param scoreboardManager the initialized scoreboard manager
     * @since 1.0
     */
    public ScoreboardManagerLoadEvent {
        Objects.requireNonNull(scoreboardManager, "scoreboard manager");
    }
}