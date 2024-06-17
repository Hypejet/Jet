package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPongConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientPongConfigurationPacket pong configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPongConfigurationPacket
 * @see PacketCodec
 */
public final class ClientPongConfigurationPacketCodec extends PacketCodec<ClientPongConfigurationPacket> {
    /**
     * Constructs a {@linkplain ClientPongConfigurationPacketCodec pong configuration packet codec}.
     *
     * @since 1.0
     */
    public ClientPongConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_PONG, ClientPongConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientPongConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientPongConfigurationPacket(buf.readInt());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientPongConfigurationPacket object) {
        buf.writeInt(object.pingIdentifier());
    }
}