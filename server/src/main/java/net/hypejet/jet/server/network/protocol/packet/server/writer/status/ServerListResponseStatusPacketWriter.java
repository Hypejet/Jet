package net.hypejet.jet.server.network.protocol.packet.server.writer.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.status.ServerListResponseStatusPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.list.ServerListPingCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter network writer}, which writes
 * {@linkplain ServerListResponseStatusPacket a server list response status packet}.
 * 
 * @since 1.0
 * @author Codestech
 * @see ServerListResponseStatusPacket
 * @see NetworkWriter
 */
public final class ServerListResponseStatusPacketWriter implements NetworkWriter<ServerListResponseStatusPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerListResponseStatusPacket object) {
        ServerListPingCodec.instance().write(buf, object.ping());
    }
}