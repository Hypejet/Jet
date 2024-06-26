package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerDisconnectConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerDisconnectConfigurationPacket disconnect configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDisconnectConfigurationPacket
 * @see PacketCodec
 */
public final class ServerDisconnectConfigurationPacketCodec extends PacketCodec<ServerDisconnectConfigurationPacket> {
    /**
     * Constructs a {@linkplain ServerDisconnectConfigurationPacketCodec disconnect configuration packet codec}.
     *
     * @since 1.0
     */
    public ServerDisconnectConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_DISCONNECT, ServerDisconnectConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerDisconnectConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerDisconnectConfigurationPacket(NetworkUtil.readComponent(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDisconnectConfigurationPacket object) {
        NetworkUtil.writeComponent(buf, object.reason());
    }
}