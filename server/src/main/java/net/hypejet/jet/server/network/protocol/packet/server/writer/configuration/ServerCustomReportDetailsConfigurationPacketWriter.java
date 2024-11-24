package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomReportDetailsConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomReportDetailsConfigurationPacket.Details;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerCustomReportDetailsConfigurationPacket a custom report detail configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCustomReportDetailsConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerCustomReportDetailsConfigurationPacketWriter
        implements NetworkWriter<ServerCustomReportDetailsConfigurationPacket> {

    private static final CollectionNetworkWriter<Details> DETAILS_COLLECTION_WRITER =
            new CollectionNetworkWriter<>(new CustomReportDetailsNetworkWriter());

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCustomReportDetailsConfigurationPacket object) {
        DETAILS_COLLECTION_WRITER.write(buf, object.detailEntries());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Details custom report details}.
     *
     * @since 1.0
     * @author Coidestech
     * @see Details
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