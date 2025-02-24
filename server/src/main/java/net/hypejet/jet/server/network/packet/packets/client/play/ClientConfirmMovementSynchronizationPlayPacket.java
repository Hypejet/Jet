package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which confirms
 * that {@linkplain net.hypejet.jet.data.model.api.coordinate.Position a position}
 * and {@linkplain net.hypejet.jet.data.model.api.coordinate.Vector vector} of a delta movement of
 * {@linkplain net.hypejet.jet.server.entity.player.JetPlayer a player} associated with a client has been synchronized
 * with the serverside value.
 *
 * @param identifier an identifier of the synchronization
 * @since 1.0
 * @see ClientPacket
 */
public record ClientConfirmMovementSynchronizationPlayPacket(int identifier) implements ClientPacket {}