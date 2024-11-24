package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerPluginMessageConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.identifier.PackedKeyNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerPluginMessageConfigurationPacket a plugin message configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerPluginMessageConfigurationPacketWriter
        implements NetworkWriter<ServerPluginMessageConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPluginMessageConfigurationPacket object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.identifier());
        buf.writeBytes(object.data());
    }
}
