package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerPingConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerPingConfigurationPacket a ping configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPingConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerPingConfigurationPacketWriter implements NetworkWriter<ServerPingConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPingConfigurationPacket object) {
        buf.writeInt(object.identifier());
    }
}
