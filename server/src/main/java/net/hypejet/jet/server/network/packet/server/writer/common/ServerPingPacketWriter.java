package net.hypejet.jet.server.network.packet.server.writer.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.server.common.ServerPingPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerPingPacket a server ping
 * packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPingPacket
 * @see NetworkWriter
 */
public final class ServerPingPacketWriter implements NetworkWriter<ServerPingPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPingPacket object) {
        buf.writeInt(object.identifier());
    }
}