package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link PacketCodec packet codec}, which reads and writes
 * a {@link ClientLoginAcknowledgeLoginPacket login acknlowledge login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginAcknowledgeLoginPacket
 * @see PacketCodec
 */
public final class ClientLoginAcknowledgeLoginPacketCodec extends PacketCodec<ClientLoginAcknowledgeLoginPacket> {
    /**
     * Constructs the {@linkplain ClientLoginAcknowledgeLoginPacketCodec login acknowledge packet codec}.
     *
     * @since 1.0
     */
    public ClientLoginAcknowledgeLoginPacketCodec() {
        super(ClientPacketIdentifiers.LOGIN_ACKNOWLEDGE, ClientLoginAcknowledgeLoginPacket.class);
    }

    @Override
    public @NonNull ClientLoginAcknowledgeLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientLoginAcknowledgeLoginPacket();
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientLoginAcknowledgeLoginPacket object) {
        // Empty, no packed data to serialize
    }
}