package net.hypejet.jet.server.network.protocol.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerDisconnectPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerDisconnectPlayPacket a disconnect play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectPlayPacket
 * @see NetworkWriter
 */
public final class ServerDisconnectPlayPacketWriter implements NetworkWriter<ServerDisconnectPlayPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectPlayPacket object) {
        ComponentNetworkCodec.instance().write(buf, object.reason());
    }
}