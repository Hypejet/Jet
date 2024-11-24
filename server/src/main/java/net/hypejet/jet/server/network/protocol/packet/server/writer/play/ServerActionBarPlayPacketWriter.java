package net.hypejet.jet.server.network.protocol.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerActionBarPlayPacket an action bar play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerActionBarPlayPacket
 * @see NetworkWriter
 */
public final class ServerActionBarPlayPacketWriter implements NetworkWriter<ServerActionBarPlayPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerActionBarPlayPacket object) {
        ComponentNetworkCodec.instance().write(buf, object.text());
    }
}