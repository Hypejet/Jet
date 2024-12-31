package net.hypejet.jet.event.events.configuration;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.network.PlayerConnectionState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when {@linkplain Player a player} switches to
 * {@linkplain PlayerConnectionState#CONFIGURATION a configuration connection state}.
 *
 * <p>Note that this event blocks configuration session thread. Unblocking it finishes the session.</p>
 *
 * @param player a player that the configuration has been started for
 * @since 1.0
 * @see PlayerConnectionState#CONFIGURATION
 */
public record ConfigurationStartEvent(@NonNull Player player) {
    /**
     * Constructs the {@linkplain ConfigurationStartEvent player configuration start event}.
     *
     * @param player a player that the configuration has been started for
     * @since 1.0
     */
    public ConfigurationStartEvent {
        NullabilityUtil.requireNonNull(player, "player");
    }
}