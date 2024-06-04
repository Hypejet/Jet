package net.hypejet.jet.server.network.protocol.reader.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgePacket;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link NetworkCodec network codec}, which reads and writes a {@link ClientLoginAcknowledgePacket login
 * acknlowledge packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginAcknowledgePacket
 * @see NetworkCodec
 */
public final class ClientLoginAcknowledgePacketReader implements NetworkCodec<ClientLoginAcknowledgePacket> {
    @Override
    public @NonNull ClientLoginAcknowledgePacket read(@NonNull ByteBuf buf) {
        return new ClientLoginAcknowledgePacket();
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientLoginAcknowledgePacket object) {
        // Empty, no packed data to serialize
    }
}