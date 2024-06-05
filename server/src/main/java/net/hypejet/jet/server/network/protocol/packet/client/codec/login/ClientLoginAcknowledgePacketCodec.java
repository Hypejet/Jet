package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgePacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link ClientPacketCodec client packet codec}, which reads and writes
 * a {@link ClientLoginAcknowledgePacket login acknlowledge packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginAcknowledgePacket
 * @see ClientPacketCodec
 */
public final class ClientLoginAcknowledgePacketCodec extends ClientPacketCodec<ClientLoginAcknowledgePacket> {
    /**
     * Constructs the {@linkplain ClientLoginAcknowledgePacketCodec login acknowledge packet codec}.
     *
     * @since 1.0
     */
    public ClientLoginAcknowledgePacketCodec() {
        super(ClientPacketIdentifiers.LOGIN_ACKNOWLEDGE, ClientLoginAcknowledgePacket.class);
    }

    @Override
    public @NonNull ClientLoginAcknowledgePacket read(@NonNull ByteBuf buf) {
        return new ClientLoginAcknowledgePacket();
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientLoginAcknowledgePacket object) {
        // Empty, no packed data to serialize
    }
}