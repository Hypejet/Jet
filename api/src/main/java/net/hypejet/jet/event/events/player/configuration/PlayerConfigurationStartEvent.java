package net.hypejet.jet.event.events.player.configuration;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.network.PlayerConnectionState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a {@linkplain Player player} is switched to
 * a {@linkplain PlayerConnectionState#CONFIGURATION configuration protocol state}.
 *
 * <p>Note that this event blocks a configuration session thread. Unblocking it will finish the session.</p>
 *
 * @param player a player that the configuration is being started for
 * @since 1.0
 * @see PlayerConnectionState#CONFIGURATION
 */
public record PlayerConfigurationStartEvent(@NonNull Player player) {
    /**
     * Constructs the {@linkplain PlayerConfigurationStartEvent player configuration start event}.
     *
     * @param player a player that the configuration is being started for
     * @since 1.0
     */
    public PlayerConfigurationStartEvent {
        NullabilityUtil.requireNonNull(player, "player");
    }
}