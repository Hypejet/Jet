package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerTransferConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerTransferConfigurationPacket transfer configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerTransferConfigurationPacket
 * @see PacketCodec
 */
public final class ServerTransferConfigurationPacketCodec extends PacketCodec<ServerTransferConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerTransferConfigurationPacketCodec transfer configuration packet codec}.
     *
     * @since 1.0
     */
    public ServerTransferConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_TRANSFER, ServerTransferConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerTransferConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerTransferConfigurationPacket(NetworkUtil.readString(buf), NetworkUtil.readVarInt(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerTransferConfigurationPacket object) {
        NetworkUtil.writeString(buf, object.host());
        NetworkUtil.writeVarInt(buf, object.port());
    }
}