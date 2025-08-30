package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.util.range.RangeUtil;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.Set;

/**
 * A {@linkplain ServerPacket server packet} updating metadata of an {@linkplain JetEntity entity}.
 *
 * @param entityId an entity whose metadata should be updated
 * @param updates metadata updates that should be performed on the metadata of the entity
 * @since 1.0
 * @see JetEntity
 * @see ServerPacket
 */
@NullMarked
public record ServerEntityMetadataPlayPacket(int entityId, Set<MetadataUpdate> updates) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerEntityMetadataPlayPacket server entity metadata packet}.
     *
     * @param entityId an entity whose metadata should be updated
     * @param updates metadata updates that should be performed on the metadata of the entity
     * @since 1.0
     */
    public ServerEntityMetadataPlayPacket {
        updates = Set.copyOf(Objects.requireNonNull(updates, "updates"));
    }

    /**
     * A field update that should be performed on metadata of an {@linkplain JetEntity entity}.
     *
     * @param index an index of the field that should be updated
     * @param dataType a data type of the field that should be updated
     * @param data a new, serialized value that should be associated the field
     * @since 1.0
     * @see JetEntity
     */
    public record MetadataUpdate(@Range(from = MIN_INDEX, to = MAX_INDEX) short index, int dataType, byte[] data) {

        private static final int MIN_INDEX = 0;
        private static final int MAX_INDEX = 254;

        /**
         * Constructs the {@linkplain MetadataUpdate metadata update}.
         *
         * @param index an index of the field that should be updated
         * @param dataType a data type of the field that should be updated
         * @param data a new, serialized value that should be associated the field
         * @since 1.0
         * @since 1.0
         */
        public MetadataUpdate {
            Objects.requireNonNull(data, "data");
            RangeUtil.ensureInRange(MIN_INDEX, MAX_INDEX, index);
        }
    }
}