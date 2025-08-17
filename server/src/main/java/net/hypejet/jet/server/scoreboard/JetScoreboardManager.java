package net.hypejet.jet.server.scoreboard;

import net.hypejet.jet.event.events.lifecycle.ScoreboardManagerLoadEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.scoreboard.ScoreboardManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents an implementation of {@linkplain ScoreboardManager a scoreboard manager}.
 *
 * @since 1.0
 * @see ScoreboardManager
 */
public final class JetScoreboardManager implements ScoreboardManager {

    private final JetScoreboard scoreboard = new JetScoreboard();

    /**
     * Constructs the {@linkplain JetScoreboardManager scoreboard manager implementation}.
     *
     * @param eventNode an event node that the scoreboard manager is being constructed for
     * @since 1.0
     */
    public JetScoreboardManager(@NonNull EventNode<Object> eventNode) {
        Objects.requireNonNull(eventNode, "event node");
        eventNode.call(new ScoreboardManagerLoadEvent(this));
    }

    @Override
    public @NonNull JetScoreboard defaultScoreboard() {
        return this.scoreboard;
    }

    @Override
    public @NonNull JetScoreboard createScoreboard() {
        return new JetScoreboard();
    }
}