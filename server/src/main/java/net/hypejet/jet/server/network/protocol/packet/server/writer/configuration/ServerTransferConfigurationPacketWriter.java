package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerTransferConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerTransferConfigurationPacket a transfer configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerTransferConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerTransferConfigurationPacketWriter implements NetworkWriter<ServerTransferConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerTransferConfigurationPacket object) {
        StringNetworkCodec.instance().write(buf, object.host());
        VarIntNetworkCodec.instance().write(buf, object.port());
    }
}