package net.hypejet.jet.server.network.packet.packets.server.play;

import io.netty.util.collection.IntCollections;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.util.range.RangeUtil;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

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
public record ServerEntityMetadataPlayPacket(int entityId, IntObjectMap<EntityMetadataValue> updates)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerEntityMetadataPlayPacket server entity metadata packet}.
     *
     * @param entityId an entity whose metadata should be updated
     * @param updates metadata updates that should be performed on the metadata of the entity
     * @since 1.0
     */
    public ServerEntityMetadataPlayPacket {
        Objects.requireNonNull(updates, "updates");
        IntObjectMap<EntityMetadataValue> updatesCopy = new IntObjectHashMap<>(updates.size());

        updates.entries().forEach(entry -> {
            int index = entry.key();
            RangeUtil.ensureInRange(0, 254, index);
            updatesCopy.put(index, entry.value());
        });

        updates = IntCollections.unmodifiableMap(updatesCopy);
    }
}