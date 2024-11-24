package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCookieRequestConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.key.PackedKeyNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerCookieRequestConfigurationPacket a cookie request configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerCookieRequestConfigurationPacketWriter
        implements NetworkWriter<ServerCookieRequestConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCookieRequestConfigurationPacket object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.identifier());
    }
}