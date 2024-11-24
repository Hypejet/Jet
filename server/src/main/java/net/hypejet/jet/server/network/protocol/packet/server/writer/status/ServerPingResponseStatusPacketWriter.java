package net.hypejet.jet.server.network.protocol.packet.server.writer.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.status.ServerPingResponseStatusPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerPingResponseStatusPacket a ping response status packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPingResponseStatusPacket
 * @see NetworkWriter
 */
public final class ServerPingResponseStatusPacketWriter implements NetworkWriter<ServerPingResponseStatusPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPingResponseStatusPacket object) {
        buf.writeLong(object.payload());
    }
}