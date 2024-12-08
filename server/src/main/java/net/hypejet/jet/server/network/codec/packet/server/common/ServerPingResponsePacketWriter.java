package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPingResponsePacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerPingResponsePacket a ping
 * response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPingResponsePacket
 * @see NetworkWriter
 */
public final class ServerPingResponsePacketWriter implements NetworkWriter<ServerPingResponsePacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPingResponsePacket object) {
        buf.writeLong(object.payload());
    }
}