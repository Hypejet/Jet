package net.hypejet.jet.server.network.protocol.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter network writer}, which writes
 * {@linkplain ServerSystemMessagePlayPacket a system message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerSystemMessagePlayPacket
 * @see NetworkWriter
 */
public final class ServerSystemMessagePlayPacketWriter implements NetworkWriter<ServerSystemMessagePlayPacket> {

    private static final int MAX_MESSAGE_SIZE = 262144;

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerSystemMessagePlayPacket object) {
        ComponentNetworkCodec.instance().write(buf, object.message());
        if (buf.readableBytes() > MAX_MESSAGE_SIZE)
            throw new IllegalArgumentException("The message size is higher than allowed");
        buf.writeBoolean(object.overlay());
    }
}