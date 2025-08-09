package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.Position;

/**
 * A {@linkplain ServerPacket server packet} updating rotation values of {@linkplain Position position}
 * of a {@linkplain JetPlayer player} that the packet being is sent to.
 *
 * @param yaw a yaw rotation value that the position should have, in degrees
 * @param pitch a pitch rotation value that the position should have, in degrees
 * @since 1.0
 * @see Position
 * @see JetPlayer
 * @see ServerPacket
 */
public record ServerSynchronizeRotationPlayPacket(float yaw, float pitch) implements ServerPacket {}