package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.Position;

/**
 * A {@linkplain ServerPacket server packet} synchronizing {@linkplain Position position}
 * of an {@linkplain JetEntity entity} when it rotated without moving.
 *
 * @param entityId an identifier of the entity whose position should be synchronized
 * @param yaw a new yaw of the position
 * @param pitch a new pitch of the position
 * @param onGround whether the entity is on ground
 * @since 1.0
 * @see Position
 * @see JetEntity
 * @see ServerPacket
 */
public record ServerEntityRotationPlayPacket(int entityId, float yaw, float pitch, boolean onGround)
        implements ServerPacket {}