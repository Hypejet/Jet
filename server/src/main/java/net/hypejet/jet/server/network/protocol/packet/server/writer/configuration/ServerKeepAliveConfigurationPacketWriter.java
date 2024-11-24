package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKeepAliveConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerKeepAliveConfigurationPacket a keep alive configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerKeepAliveConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerKeepAliveConfigurationPacketWriter implements NetworkWriter<ServerKeepAliveConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerKeepAliveConfigurationPacket object) {
        buf.writeLong(object.keepAliveIdentifier());
    }
}
