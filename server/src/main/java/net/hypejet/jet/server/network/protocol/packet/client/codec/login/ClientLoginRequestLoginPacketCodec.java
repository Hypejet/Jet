package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.session.JetLoginSession;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link ClientPacketCodec client packet codec}, which reads and writes a {@link ClientLoginRequestLoginPacket login
 * request login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginRequestLoginPacket
 * @see ClientPacketCodec
 */
public final class ClientLoginRequestLoginPacketCodec extends ClientPacketCodec<ClientLoginRequestLoginPacket> {
    /**
     * Constructs a {@linkplain ClientLoginRequestLoginPacketCodec login request packet codec}..
     *
     * @since 1.0
     */
    public ClientLoginRequestLoginPacketCodec() {
        super(ClientPacketIdentifiers.LOGIN_REQUEST, ClientLoginRequestLoginPacket.class);
    }

    @Override
    public @NonNull ClientLoginRequestLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientLoginRequestLoginPacket(NetworkUtil.readString(buf), NetworkUtil.readUniqueId(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientLoginRequestLoginPacket object) {
        NetworkUtil.writeString(buf, object.username());
        NetworkUtil.writeUniqueId(buf, object.uniqueId());
    }

    @Override
    public void handle(@NonNull ClientLoginRequestLoginPacket packet, @NonNull SocketPlayerConnection connection) {
        JetLoginSession session = JetLoginSession.asLoginSession(connection.getSession());
        session.sessionHandler().onLoginRequest(packet, session);
    }
}