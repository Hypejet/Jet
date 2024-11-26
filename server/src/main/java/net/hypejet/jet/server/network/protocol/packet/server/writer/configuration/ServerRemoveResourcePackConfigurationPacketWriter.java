package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRemoveResourcePackConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerRemoveResourcePackConfigurationPacket a remove resource pack configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerRemoveResourcePackConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerRemoveResourcePackConfigurationPacketWriter
        implements NetworkWriter<ServerRemoveResourcePackConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerRemoveResourcePackConfigurationPacket object) {
        NetworkUtil.writeOptional(object.uniqueId(), UUIDNetworkCodec.INSTANCE, buf);
    }
}