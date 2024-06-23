package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKeepAliveConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerKeepAliveConfigurationPacket keep alive configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerKeepAliveConfigurationPacket
 * @see PacketCodec
 */
public final class ServerKeepAliveConfigurationPacketCodec extends PacketCodec<ServerKeepAliveConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerKeepAliveConfigurationPacketCodec keep alive configuration packet codec}.
     *
     * @since 1.0
     */
    public ServerKeepAliveConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_KEEP_ALIVE, ServerKeepAliveConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerKeepAliveConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerKeepAliveConfigurationPacket(buf.readLong());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerKeepAliveConfigurationPacket object) {
        buf.writeLong(object.keepAliveIdentifier());
    }
}
