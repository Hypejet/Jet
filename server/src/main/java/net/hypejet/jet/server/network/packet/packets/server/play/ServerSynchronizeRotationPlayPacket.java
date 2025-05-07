package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet}, which updates rotation values
 * of {@linkplain net.hypejet.jet.data.model.api.coordinate.Position a position}
 * of {@linkplain net.hypejet.jet.server.entity.player.JetPlayer a player} associated with a client that the packet
 * is sent to.
 *
 * @param yaw a yaw rotation value that the position should have, in degrees
 * @param pitch a pitch rotation value that the position should have, in degrees
 * @since 1.0
 * @see ServerPacket
 * @see net.hypejet.jet.data.model.api.coordinate.Position
 * @see net.hypejet.jet.server.entity.player.JetPlayer
 */
public record ServerSynchronizeRotationPlayPacket(float yaw, float pitch) implements ServerPacket {}