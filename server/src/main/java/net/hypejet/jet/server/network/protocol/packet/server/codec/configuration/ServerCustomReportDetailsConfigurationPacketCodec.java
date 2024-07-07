package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomReportDetailsConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.report.CustomReportDetailsNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerCustomReportDetailsConfigurationPacket custom report detail configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCustomReportDetailsConfigurationPacket
 * @see PacketCodec
 */
public final class ServerCustomReportDetailsConfigurationPacketCodec
        extends PacketCodec<ServerCustomReportDetailsConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerCustomReportDetailsConfigurationPacketCodec custom report details configuration
     * packet codec}.
     *
     * @since 1.0
     */
    public ServerCustomReportDetailsConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_CUSTOM_REPORT_DETAILS,
                ServerCustomReportDetailsConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerCustomReportDetailsConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerCustomReportDetailsConfigurationPacket(
                NetworkUtil.readCollection(buf, CustomReportDetailsNetworkCodec.instance())
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCustomReportDetailsConfigurationPacket object) {
        NetworkUtil.writeCollection(buf, CustomReportDetailsNetworkCodec.instance(), object.detailEntries());
    }
}