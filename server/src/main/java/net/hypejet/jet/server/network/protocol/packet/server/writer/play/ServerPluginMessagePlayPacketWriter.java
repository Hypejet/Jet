package net.hypejet.jet.server.network.protocol.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerPluginMessagePlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.identifier.PackedKeyNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerPluginMessagePlayPacket a plugin message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessagePlayPacket
 * @see NetworkWriter
 */
public final class ServerPluginMessagePlayPacketWriter implements NetworkWriter<ServerPluginMessagePlayPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPluginMessagePlayPacket object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.identifier());
        buf.writeBytes(object.data());
    }
}
