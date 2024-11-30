package net.hypejet.jet.event.events.player;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.network.packet.server.common.ServerPingPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a {@linkplain Player player} responds to
 * a {@linkplain ServerPingPacket ping configuration
 * packet}.
 *
 * @param pingIdentifier an identifier of the ping that the player responds to
 * @since 1.0
 * @author Codestech
 * @see ServerPingPacket
 */
public record PlayerPongEvent(@NonNull Player player, int pingIdentifier) {}