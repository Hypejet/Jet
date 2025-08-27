package net.hypejet.jet.server.network.packet.packets.server.play;

import java.util.Objects;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.NonNull;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;

/**
 * Represents {@linkplain ServerPacket a server packet} that requests a client
 * to spawn an entity in the world.
 *
 * @param entityId the entity ID
 * @param uniqueId the unique UUID of the entity
 * @param type the entity type ID
 * @param position the initial spawn {@linkplain Position position} of the entity
 * @param headYaw the initial yaw (rotation) of the entity's head
 * @param data additional entity-specific data
 * @param velocity the initial {@linkplain Vector velocity} of the entity
 * @since 1.0
 * @see ServerPacket
 */
public record ServerSpawnEntityPlayPacket(
        int entityId, @NonNull UUID uniqueId, int type,
        @NonNull Position position, float headYaw, int data,
        @NonNull Vector velocity
) implements ServerPacket {

    /**
     * Constructs the {@linkplain ServerSpawnEntityPlayPacket server spawn entity play packet}.
     *
     * @param entityId the entity ID
     * @param uniqueId the unique UUID of the entity
     * @param type the entity type ID
     * @param position the initial spawn {@linkplain Position position} of the entity
     * @param headYaw the initial yaw (rotation) of the entity's head
     * @param data additional entity-specific data
     * @param velocity the initial {@linkplain Vector velocity} of the entity
     * @since 1.0
     */
    public ServerSpawnEntityPlayPacket {
        Objects.requireNonNull(uniqueId, "uniqueId");
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(velocity, "velocity");
    }
}
