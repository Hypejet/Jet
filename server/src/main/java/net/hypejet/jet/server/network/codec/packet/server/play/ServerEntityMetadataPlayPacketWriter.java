package net.hypejet.jet.server.network.codec.packet.server.play;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityMetadataPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerEntityMetadataPlayPacket server entity metadata play packets}.
 *
 * @since 1.0
 * @see NetworkWriter
 * @see ServerEntityMetadataPlayPacket
 */
public final class ServerEntityMetadataPlayPacketWriter implements NetworkWriter<ServerEntityMetadataPlayPacket> {

    private static final CollectionNetworkWriter<IntObjectMap.PrimitiveEntry<EntityMetadataValue>>
            UPDATES_WRITER = new CollectionNetworkWriter<>(false, new MetadataUpdateNetworkWriter());

    /**
     * An instance of the {@linkplain ServerEntityMetadataPlayPacketWriter server entity metadata play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntityMetadataPlayPacketWriter INSTANCE = new ServerEntityMetadataPlayPacketWriter();

    private static final Object2IntMap<Class<? extends EntityMetadataValue>>
            DATA_TYPE_IDS = new Object2IntOpenHashMap<>();

    private ServerEntityMetadataPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEntityMetadataPlayPacket object) {
        UPDATES_WRITER.write(buf, Sets.newHashSet(object.updates().entries()));
        buf.writeByte(255); // Mark an end of metadata updates
    }

    /**
     * A {@linkplain NetworkWriter network writer}
     * of {@linkplain IntObjectMap.PrimitiveEntry int-object map primitive entries}
     * associating {@linkplain JetEntityMetadata entity metadata} indices
     * with {@linkplain EntityMetadataValue entity metadata values} bound to them.
     *
     * @since 1.0
     * @see JetEntityMetadata
     * @see NetworkWriter
     */
    private static final class MetadataUpdateNetworkWriter
            implements NetworkWriter<IntObjectMap.PrimitiveEntry<EntityMetadataValue>> {
        @Override
        public void write(@NonNull ByteBuf buf, IntObjectMap.@NonNull PrimitiveEntry<EntityMetadataValue> object) {
            buf.writeByte(object.key());

            EntityMetadataValue value = object.value();
            Class<? extends EntityMetadataValue> valueClass = value.getClass();

            if (!DATA_TYPE_IDS.containsKey(valueClass)) {
                throw new IllegalArgumentException(String.format(
                        "No data type ID was specified for entity metadata value with class: "
                                + valueClass.getSimpleName()
                ));
            }

            buf.writeInt(DATA_TYPE_IDS.getInt(valueClass));
            // TODO: Serialize data
        }
    }
}