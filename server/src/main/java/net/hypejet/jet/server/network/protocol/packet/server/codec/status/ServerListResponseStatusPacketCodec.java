package net.hypejet.jet.server.network.protocol.packet.server.codec.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.status.ServerListResponseStatusPacket;
import net.hypejet.jet.server.network.protocol.codecs.list.ServerListPingCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerListResponseStatusPacket server list response status packet}.
 * 
 * @since 1.0
 * @author Codestech
 * @see ServerListResponseStatusPacket
 * @see PacketCodec
 */
public final class ServerListResponseStatusPacketCodec extends PacketCodec<ServerListResponseStatusPacket> {
    /**
     * Constructs the {@linkplain ServerListResponseStatusPacketCodec server list response status packet codec}.
     *
     * @since 1.0
     */
    public ServerListResponseStatusPacketCodec() {
        super(ServerPacketIdentifiers.STATUS_SERVER_LIST_RESPONSE, ServerListResponseStatusPacket.class);
    }

    @Override
    public @NonNull ServerListResponseStatusPacket read(@NonNull ByteBuf buf) {
        return new ServerListResponseStatusPacket(ServerListPingCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerListResponseStatusPacket object) {
        ServerListPingCodec.instance().write(buf, object.ping());
    }
}
