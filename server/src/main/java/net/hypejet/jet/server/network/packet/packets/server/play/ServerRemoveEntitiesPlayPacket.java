package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A {@linkplain ServerPacket server packet} removing the specified {@linkplain JetEntity entities}.
 *
 * @param entityIds identifiers of the entities to remove
 * @since 1.0
 * @see JetEntity
 * @see ServerPacket
 */
public record ServerRemoveEntitiesPlayPacket(int @NonNull ... entityIds) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerRemoveEntitiesPlayPacket server remove entities play packet}.
     *
     * @param entityIds identifiers of the entities to remove
     * @since 1.0
     */
    public ServerRemoveEntitiesPlayPacket {
        Objects.requireNonNull(entityIds, "entity ids");
    }
}