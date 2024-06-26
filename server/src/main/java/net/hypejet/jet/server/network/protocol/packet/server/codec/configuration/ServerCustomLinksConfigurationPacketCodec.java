package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomLinksConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.link.ServerLinkNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * the {@linkplain ServerCustomLinksConfigurationPacket custom links configuration packet}.
 *
 * @since 1.0
 * @author Codestech@
 * @see ServerCustomLinksConfigurationPacket
 * @see PacketCodec
 */
public final class ServerCustomLinksConfigurationPacketCodec
        extends PacketCodec<ServerCustomLinksConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerCustomLinksConfigurationPacketCodec custom links configuration packet codec}.
     *
     * @since 1.0
     */
    public ServerCustomLinksConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_SERVER_LINKS, ServerCustomLinksConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerCustomLinksConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerCustomLinksConfigurationPacket(NetworkUtil.readCollection(buf, ServerLinkNetworkCodec.instance()));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCustomLinksConfigurationPacket object) {
        NetworkUtil.writeCollection(buf, ServerLinkNetworkCodec.instance(), object.serverLinks());
    }
}