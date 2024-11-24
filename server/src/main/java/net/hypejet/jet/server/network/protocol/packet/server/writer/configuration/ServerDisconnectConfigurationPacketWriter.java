package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerDisconnectConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerDisconnectConfigurationPacket a disconnect configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerDisconnectConfigurationPacketWriter
        implements NetworkWriter<ServerDisconnectConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectConfigurationPacket object) {
        ComponentNetworkWriter.INSTANCE.write(buf, object.reason());
    }
}