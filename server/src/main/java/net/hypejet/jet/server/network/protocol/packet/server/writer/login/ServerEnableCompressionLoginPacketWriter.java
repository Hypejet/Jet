package net.hypejet.jet.server.network.protocol.packet.server.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerEnableCompressionLoginPacket an enable compression login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerEnableCompressionLoginPacket
 * @see NetworkWriter
 */
public final class ServerEnableCompressionLoginPacketWriter
        implements NetworkWriter<ServerEnableCompressionLoginPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEnableCompressionLoginPacket object) {
        VarIntNetworkCodec.instance().write(buf, object.threshold());
    }
}
