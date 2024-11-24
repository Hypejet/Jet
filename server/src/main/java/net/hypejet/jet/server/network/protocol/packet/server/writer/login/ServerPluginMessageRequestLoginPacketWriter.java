package net.hypejet.jet.server.network.protocol.packet.server.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestLoginPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerPluginMessageRequestLoginPacket a plugin message request login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageRequestLoginPacket
 * @see NetworkWriter
 */
public final class ServerPluginMessageRequestLoginPacketWriter
        implements NetworkWriter<ServerPluginMessageRequestLoginPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPluginMessageRequestLoginPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.messageId());
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.channel());
        buf.writeBytes(object.data());
    }
}
