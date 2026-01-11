package net.hypejet.jet.server.network.packet.packets.server.play;

import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.util.range.RangeUtil;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A {@linkplain ServerPacket server packet} updating metadata of an {@linkplain JetEntity entity}.
 *
 * @param entityId numeric identifier of entity whose metadata should be updated
 * @param updates metadata updates that should be performed on the metadata of the entity
 * @since 1.0
 * @see JetEntity
 * @see ServerPacket
 */
@NullMarked
public record ServerEntityMetadataPlayPacket(int entityId, Set<Update> updates) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerEntityMetadataPlayPacket server entity metadata packet}.
     *
     * @param entityId numeric identifier of entity whose metadata should be updated
     * @param updates metadata updates that should be performed on the metadata of the entity
     * @since 1.0
     */
    public ServerEntityMetadataPlayPacket {
        updates = Set.copyOf(Objects.requireNonNull(updates, "updates"));
    }

    /**
     * Creates a {@linkplain ServerEntityMetadataPlayPacket server entity metadata play packet}
     * using the specified update map.
     *
     * @param entityId numeric identifier of entity whose metadata should be updated
     * @param updateMap the update map to create the packet with
     * @return the created packet
     * @since 1.0
     */
    public static ServerEntityMetadataPlayPacket create(int entityId, IntObjectMap<EntityMetadataValue> updateMap) {
        Set<Update> updates = new HashSet<>();
        updateMap.forEach((index, value) -> updates.add(new Update(index, value)));
        return new ServerEntityMetadataPlayPacket(entityId, updates);
    }

    /**
     * A value update that should be applied to entity metadata.
     *
     * @param index the index at which the update should be applied
     * @param value the new value that should be present at the specified index
     * @since 1.0
     */
    public record Update(int index, EntityMetadataValue value) {
        /**
         * Constructs the {@linkplain Update update}.
         *
         * @param index the index at which the update should be applied
         * @param value the new value that should be present at the specified index
         * @since 1.0
         */
        public Update {
            RangeUtil.ensureInRange(0, 254, index);
            Objects.requireNonNull(value, "value");
        }
    }
}