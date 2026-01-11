package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A {@linkplain ServerPacket server packet} synchronizing {@linkplain Position position}
 * and velocity {@linkplain Vector vector} of an {@linkplain JetEntity entity}.
 *
 * @param entityId an identifier of the entity whose position and velocity should be synchronized
 * @param position the new position of the entity
 * @param velocity the new velocity vector of the entity
 * @param onGround whether the entity is on ground
 * @since 1.0
 * @see Position
 * @see Vector
 * @see JetEntity
 * @see ServerPacket
 */
@NullMarked
public record ServerSynchronizeEntityPositionPlayPacket(int entityId, Position position,
                                                        Vector velocity, boolean onGround) implements ServerPacket {
    /**
     * Constructs
     * the {@linkplain ServerSynchronizeEntityPositionPlayPacket server synchronize entity position play packet}.
     *
     * @param entityId an identifier of the entity whose position and velocity should be synchronized
     * @param position the new position of the entity
     * @param velocity the new velocity vector of the entity
     * @param onGround whether the entity is on ground
     * @since 1.0
     */
    public ServerSynchronizeEntityPositionPlayPacket {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(velocity, "velocity");
    }
}