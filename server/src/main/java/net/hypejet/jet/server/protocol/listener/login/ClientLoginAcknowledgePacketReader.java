package net.hypejet.jet.server.protocol.listener.login;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.login.ClientLoginAcknowledgePacket;
import net.hypejet.jet.server.protocol.listener.EmptyPacketReader;

/**
 * Represents a {@link EmptyPacketReader empty packet reader}, which reads a
 * {@link ClientLoginAcknowledgePacket login acknlowledge packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginAcknowledgePacket
 * @see EmptyPacketReader
 */
public final class ClientLoginAcknowledgePacketReader extends EmptyPacketReader<ClientLoginAcknowledgePacket> {

    /**
     * Constructs a {@link ClientLoginAcknowledgePacketReader login acknowledge packet reader}.
     *
     * @since 1.0
     */
    public ClientLoginAcknowledgePacketReader() {
        super(3, ProtocolState.LOGIN, ClientLoginAcknowledgePacketImpl::new);
    }

    /**
     * Represents an implementation of {@link ClientLoginAcknowledgePacket login acknowledge packet}.
     *
     * @since 1.0
     * @author Codestech
     * @see ClientLoginAcknowledgePacket
     */
    private record ClientLoginAcknowledgePacketImpl() implements ClientLoginAcknowledgePacket {}
}