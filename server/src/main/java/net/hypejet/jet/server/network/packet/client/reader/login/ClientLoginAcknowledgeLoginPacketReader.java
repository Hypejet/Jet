package net.hypejet.jet.server.network.packet.client.reader.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientLoginAcknowledgeLoginPacket a login acknowledge login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginAcknowledgeLoginPacket
 * @see NetworkReader
 */
public final class ClientLoginAcknowledgeLoginPacketReader
        implements NetworkReader<ClientLoginAcknowledgeLoginPacket> {
    @Override
    public @NonNull ClientLoginAcknowledgeLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientLoginAcknowledgeLoginPacket();
    }
}