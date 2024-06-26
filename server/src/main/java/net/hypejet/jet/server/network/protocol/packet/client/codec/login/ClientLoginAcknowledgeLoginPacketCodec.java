package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.session.JetConfigurationSession;
import net.hypejet.jet.server.session.JetLoginSession;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientLoginAcknowledgeLoginPacket login acknowledge login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginAcknowledgeLoginPacket
 * @see ClientPacketCodec
 */
public final class ClientLoginAcknowledgeLoginPacketCodec
        extends ClientPacketCodec<ClientLoginAcknowledgeLoginPacket> {
    /**
     * Constructs the {@linkplain ClientLoginAcknowledgeLoginPacketCodec login acknowledge login packet codec}.
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
        // NOOP
    }

    @Override
    public void handle(@NonNull ClientLoginAcknowledgeLoginPacket packet, @NonNull SocketPlayerConnection connection) {
        JetLoginSession session = JetLoginSession.asLoginSession(connection.getSession());

        session.releaseAcknowledgeLatch();
        session.sessionHandler().onLoginAcknowledge(packet, session);

        connection.setProtocolState(ProtocolState.CONFIGURATION);

        JetPlayer player = Objects.requireNonNull(connection.player(), "Player cannot be null");
        connection.setSession(new JetConfigurationSession(player));
    }
}