package net.hypejet.jet.server.network.packet.packets.server.play;

import java.util.Objects;
import java.util.UUID;

import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.world.JetWorld;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain ServerPacket server packet} requesting a client to spawn
 * an {@linkplain JetEntity entity} in the {@linkplain JetWorld world}.
 *
 * @param entityId an identifier of the entity that is being spawned
 * @param uniqueId a unique identifier of the entity that is being spawned
 * @param type an identifier of type of the entity that is being spawned
 * @param position an initial position that the entity should spawn at
 * @param headYaw an initial head yaw that the entity should have
 * @param data an additional entity-specific data
 * @param velocity an initial velocity vector that the entity should have
 * @since 1.0
 * @see JetEntity
 * @see JetWorld
 * @see ServerPacket
 */
@NullMarked
public record ServerSpawnEntityPlayPacket(int entityId, UUID uniqueId, int type, Position position,
                                          float headYaw, int data, Vector velocity) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerSpawnEntityPlayPacket server spawn entity play packet}.
     *
     * @param entityId an identifier of the entity that is being spawned
     * @param uniqueId a unique identifier of the entity that is being spawned
     * @param type an identifier of type of the entity that is being spawned
     * @param position an initial position that the entity should spawn at
     * @param headYaw an initial head yaw that the entity should have
     * @param data an additional entity-specific data
     * @param velocity an initial velocity vector that the entity should have
     * @since 1.0
     */
    public ServerSpawnEntityPlayPacket {
        Objects.requireNonNull(uniqueId, "uniqueId");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(velocity, "velocity");
    }
}
