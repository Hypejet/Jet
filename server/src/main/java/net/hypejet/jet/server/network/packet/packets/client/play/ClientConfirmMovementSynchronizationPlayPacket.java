package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;

/**
 * A {@linkplain ClientPacket client packet} confirming that {@linkplain Position position}
 * and delta-movement {@linkplain Vector} of a {@linkplain net.hypejet.jet.server.entity.player.JetPlayer player}
 * has been synchronized with the serverside value.
 *
 * @param identifier an identifier of the synchronization
 * @since 1.0
 * @see ClientPacket
 */
public record ClientConfirmMovementSynchronizationPlayPacket(int identifier) implements ClientPacket {}