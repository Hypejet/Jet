package net.hypejet.jet.event.events.player.configuration;

import net.hypejet.jet.entity.player.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a {@linkplain Player player} is switched to
 * a {@linkplain net.hypejet.jet.protocol.ProtocolState#CONFIGURATION configuration protocol state}.
 *
 * <p>Note that this event blocks a configuration session thread. Unblocking it will finish the session.</p>
 *
 * @param player the player
 * @since 1.0
 * @see net.hypejet.jet.protocol.ProtocolState#CONFIGURATION
 */
public record PlayerConfigurationStartEvent(@NonNull Player player) {}