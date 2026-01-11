package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.Vector;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A {@linkplain ServerPacket server packet} updating a velocity {@linkplain Vector vector}
 * of an {@linkplain JetEntity entity}.
 *
 * @param entityId an identifier of the entity whose velocity vector should be updates
 * @param velocity the velocity vector that the entity should have
 * @since 1.0
 * @see Vector
 * @see JetEntity
 * @see ServerPacket
 */
public record ServerEntityVelocityPlayPacket(int entityId, @NonNull Vector velocity) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerEntityVelocityPlayPacket server entity velocity play packet}.
     *
     * @param entityId an identifier of the entity whose velocity vector should be updates
     * @param velocity the velocity vector that the entity should have
     * @since 1.0
     */
    public ServerEntityVelocityPlayPacket {
        Objects.requireNonNull(velocity, "velocity");
    }
}