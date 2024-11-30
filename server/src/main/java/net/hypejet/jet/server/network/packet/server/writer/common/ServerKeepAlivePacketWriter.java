package net.hypejet.jet.server.network.packet.server.writer.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.server.common.ServerKeepAlivePacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerKeepAlivePacket a server
 * keep alive packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerKeepAlivePacket
 * @see NetworkWriter
 */
public final class ServerKeepAlivePacketWriter implements NetworkWriter<ServerKeepAlivePacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerKeepAlivePacket object) {
        buf.writeLong(object.keepAliveIdentifier());
    }
}