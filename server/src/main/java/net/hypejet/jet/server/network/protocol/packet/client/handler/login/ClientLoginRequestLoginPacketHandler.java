package net.hypejet.jet.server.network.protocol.packet.client.handler.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.LoginTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@link ClientPacketHandler a client packet handler}, which reads and handles
 * {@link ClientLoginRequestLoginPacket a login request login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginRequestLoginPacket
 * @see ClientPacketHandler
 */
public final class ClientLoginRequestLoginPacketHandler implements ClientPacketHandler<ClientLoginRequestLoginPacket> {

    private static final StringNetworkCodec USERNAME_CODEC = StringNetworkCodec.create(16);

    @Override
    public @NonNull ClientLoginRequestLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientLoginRequestLoginPacket(USERNAME_CODEC.read(buf), UUIDNetworkCodec.instance().read(buf));
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