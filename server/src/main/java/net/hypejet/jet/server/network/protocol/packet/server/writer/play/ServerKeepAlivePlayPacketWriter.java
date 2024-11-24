package net.hypejet.jet.server.network.protocol.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerKeepAlivePlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which reads and writes
 * {@linkplain ServerKeepAlivePlayPacket a keep alive play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerKeepAlivePlayPacket
 * @see NetworkWriter
 */
public final class ServerKeepAlivePlayPacketWriter implements NetworkWriter<ServerKeepAlivePlayPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerKeepAlivePlayPacket object) {
        buf.writeLong(object.keepAliveIdentifier());
    }
}