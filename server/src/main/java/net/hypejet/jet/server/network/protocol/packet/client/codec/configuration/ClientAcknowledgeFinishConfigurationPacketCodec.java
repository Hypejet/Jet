package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.configuration.ClientAcknowledgeFinishConfigurationPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.session.JetConfigurationSession;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientAcknowledgeFinishConfigurationPacket client acknowledge finish configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientAcknowledgeFinishConfigurationPacket
 * @see ClientPacketCodec
 */
public final class ClientAcknowledgeFinishConfigurationPacketCodec
        extends ClientPacketCodec<ClientAcknowledgeFinishConfigurationPacket> {
    /**
     * Constructs the {@linkplain ClientAcknowledgeFinishConfigurationPacketCodec client acknowledge finish
     * configuration packet codec}.
     *
     * @since 1.0
     */
    public ClientAcknowledgeFinishConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_ACKNOWLEDGE_FINISH_CONFIGURATION,
                ClientAcknowledgeFinishConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientAcknowledgeFinishConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientAcknowledgeFinishConfigurationPacket();
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientAcknowledgeFinishConfigurationPacket object) {
        // NOOP
    }

    @Override
    public void handle(@NonNull ClientAcknowledgeFinishConfigurationPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        JetConfigurationSession session = JetConfigurationSession.asConfigurationSession(connection.getSession());
        session.handleFinishAcknowledge();

        connection.setProtocolState(ProtocolState.PLAY);
        // TODO: Switch to a play session
    }
}