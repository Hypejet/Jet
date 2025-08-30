package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityMetadataPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityMetadataPlayPacket.MetadataUpdate;
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

    private static final CollectionNetworkWriter<MetadataUpdate>
            UPDATES_WRITER = new CollectionNetworkWriter<>(false, new MetadataUpdateNetworkWriter());

    /**
     * An instance of the {@linkplain ServerEntityMetadataPlayPacketWriter server entity metadata play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntityMetadataPlayPacketWriter INSTANCE = new ServerEntityMetadataPlayPacketWriter();

    private ServerEntityMetadataPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEntityMetadataPlayPacket object) {
        UPDATES_WRITER.write(buf, object.updates());
        buf.writeByte(255); // Mark an end of metadata updates
    }

    /**
     * A {@linkplain NetworkWriter network writer} of {@linkplain MetadataUpdate metadata updates}.
     *
     * @since 1.0
     * @see MetadataUpdate
     * @see NetworkWriter
     */
    private static final class MetadataUpdateNetworkWriter implements NetworkWriter<MetadataUpdate> {
        @Override
        public void write(@NonNull ByteBuf buf, @NonNull MetadataUpdate object) {
            buf.writeByte(object.dataType());
            VarIntNetworkCodec.INSTANCE.write(buf, object.dataType());
            buf.writeBytes(object.data());
        }
    }
}