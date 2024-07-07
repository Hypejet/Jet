package net.hypejet.jet.event.events.player;

import net.hypejet.jet.entity.player.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a {@linkplain Player player} responds to
 * a {@linkplain net.hypejet.jet.protocol.packet.server.configuration.ServerPingConfigurationPacket ping configuration
 * packet}.
 *
 * @param pingIdentifier an identifier of the ping that the player responds to
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.configuration.ServerPingConfigurationPacket
 */
public record PlayerPongEvent(@NonNull Player player, int pingIdentifier) {}