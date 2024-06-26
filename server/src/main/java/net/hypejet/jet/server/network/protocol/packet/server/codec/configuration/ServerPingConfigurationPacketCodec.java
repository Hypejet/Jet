package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerPingConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and write
 * a {@linkplain ServerPingConfigurationPacket ping configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPingConfigurationPacket
 * @see PacketCodec
 */
public final class ServerPingConfigurationPacketCodec extends PacketCodec<ServerPingConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerPingConfigurationPacketCodec ping configuration packet codec}.
     *
     * @since 1.0
     */
    public ServerPingConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_PING, ServerPingConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerPingConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerPingConfigurationPacket(buf.readInt());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPingConfigurationPacket object) {
        buf.writeInt(object.identifier());
    }
}
