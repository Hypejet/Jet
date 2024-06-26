package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponseLoginPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.session.JetLoginSession;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link ClientPacketCodec client packet codec}, which reads and writes
 * a {@link ClientEncryptionResponseLoginPacket encryption response login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientEncryptionResponseLoginPacket
 * @see ClientPacketCodec
 */
public final class ClientEncryptionResponseLoginPacketCodec
        extends ClientPacketCodec<ClientEncryptionResponseLoginPacket> {
    /**
     * Constructs the {@linkplain ClientEncryptionResponseLoginPacketCodec encryption response packet codec}.
     *
     * @since 1.0
     */
    public ClientEncryptionResponseLoginPacketCodec() {
        super(ClientPacketIdentifiers.LOGIN_ENCRYPTION_RESPONSE, ClientEncryptionResponseLoginPacket.class);
    }

    @Override
    public @NonNull ClientEncryptionResponseLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientEncryptionResponseLoginPacket(NetworkUtil.readByteArray(buf), NetworkUtil.readByteArray(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientEncryptionResponseLoginPacket object) {
        NetworkUtil.writeByteArray(buf, object.sharedSecret());
        NetworkUtil.writeByteArray(buf, object.verifyToken());
    }

    @Override
    public void handle(@NonNull ClientEncryptionResponseLoginPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        JetLoginSession session = JetLoginSession.asLoginSession(connection.getSession());
        session.sessionHandler().onEncryptionResponse(packet, session);
    }
}