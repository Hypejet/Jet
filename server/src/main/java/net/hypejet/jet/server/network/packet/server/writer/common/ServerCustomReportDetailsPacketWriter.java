package net.hypejet.jet.server.network.packet.server.writer.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.server.common.ServerCustomReportDetailsPacket;
import net.hypejet.jet.network.packet.server.common.ServerCustomReportDetailsPacket.Details;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerCustomReportDetailsPacket a server custom report details packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCustomReportDetailsPacket
 * @see NetworkWriter
 */
public final class ServerCustomReportDetailsPacketWriter implements NetworkWriter<ServerCustomReportDetailsPacket> {

    private static final CollectionNetworkWriter<Details> DETAILS_COLLECTION_WRITER
            = new CollectionNetworkWriter<>(new CustomReportDetailsNetworkWriter());

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCustomReportDetailsPacket object) {
        DETAILS_COLLECTION_WRITER.write(buf, object.details());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Details crash report details}.
     *
     * @since 1.0
     * @author Coidestech
     * @see ServerCustomReportDetailsPacketWriter
     * @see ServerCustomReportDetailsPacket
     * @see NetworkWriter
     */
    private static final class CustomReportDetailsNetworkWriter implements NetworkWriter<Details> {

        private static final StringNetworkCodec TITLE_CODEC = StringNetworkCodec.create(128);
        private static final StringNetworkCodec DESCRIPTION_CODEC = StringNetworkCodec.create(4096);

        private CustomReportDetailsNetworkWriter() {}

        @Override
        public void write(@NonNull ByteBuf buf, @NonNull Details object) {
            TITLE_CODEC.write(buf, object.title());
            DESCRIPTION_CODEC.write(buf, object.description());
        }
    }
}