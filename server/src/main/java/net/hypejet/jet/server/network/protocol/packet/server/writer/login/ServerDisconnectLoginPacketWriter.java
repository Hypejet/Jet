package net.hypejet.jet.server.network.protocol.packet.server.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectLoginPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.component.JsonComponentNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerDisconnectLoginPacket a disconnect login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectLoginPacket
 * @see NetworkWriter
 */
public final class ServerDisconnectLoginPacketWriter implements NetworkWriter<ServerDisconnectLoginPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectLoginPacket object) {
        JsonComponentNetworkCodec.instance().write(buf, object.reason());
    }
}