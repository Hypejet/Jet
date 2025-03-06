package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerCustomReportDetailsPacket;
import net.hypejet.jet.util.game.crash.CrashReportDetails;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerCustomReportDetailsPacket a server custom report details packet}.
 *
 * @since 1.0
 * @see ServerCustomReportDetailsPacket
 * @see NetworkWriter
 */
public final class ServerCustomReportDetailsPacketWriter implements NetworkWriter<ServerCustomReportDetailsPacket> {

    /**
     * An instance of the {@linkplain ServerCustomReportDetailsPacketWriter server custom report details packet
     * writer}.
     *
     * @since 1.0
     */
    public static final ServerCustomReportDetailsPacketWriter INSTANCE = new ServerCustomReportDetailsPacketWriter();

    private static final CollectionNetworkWriter<CrashReportDetails> DETAILS_COLLECTION_WRITER
            = new CollectionNetworkWriter<>(new CustomReportDetailsNetworkWriter());

    private ServerCustomReportDetailsPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCustomReportDetailsPacket object) {
        DETAILS_COLLECTION_WRITER.write(buf, object.details());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which writes
     * {@linkplain CrashReportDetails crash report details}.
     *
     * @since 1.0
     * @see CrashReportDetails
     * @see NetworkWriter
     */
    private static final class CustomReportDetailsNetworkWriter implements NetworkWriter<CrashReportDetails> {

        private static final StringNetworkCodec TITLE_CODEC = StringNetworkCodec.create(128);
        private static final StringNetworkCodec DESCRIPTION_CODEC = StringNetworkCodec.create(4096);

        private CustomReportDetailsNetworkWriter() {}

        @Override
        public void write(@NonNull ByteBuf buf, @NonNull CrashReportDetails object) {
            TITLE_CODEC.write(buf, object.title());
            DESCRIPTION_CODEC.write(buf, object.description());
        }
    }
}