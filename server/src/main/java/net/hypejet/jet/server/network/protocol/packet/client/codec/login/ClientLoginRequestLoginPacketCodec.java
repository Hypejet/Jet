package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.session.task.LoginTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
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

    private static final StringNetworkCodec USERNAME_CODEC = StringNetworkCodec.create(16);

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
        return new ClientLoginRequestLoginPacket(USERNAME_CODEC.read(buf), UUIDNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientLoginRequestLoginPacket object) {
        USERNAME_CODEC.write(buf, object.username());
        UUIDNetworkCodec.instance().write(buf, object.uniqueId());
    }

    @Override
    public void handle(@NonNull ClientLoginRequestLoginPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof LoginTask loginTask))
            throw new IllegalArgumentException("The current session task must be a login task");

        SocketPlayerConnection connection = loginTask.connection();
        connection.setCompressionThreshold(connection.server()
                .configuration()
                .compressionThreshold());

        loginTask.handlePacket(packet);
    }
}