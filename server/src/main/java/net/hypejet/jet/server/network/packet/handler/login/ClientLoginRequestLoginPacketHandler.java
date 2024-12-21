package net.hypejet.jet.server.network.packet.handler.login;

import net.hypejet.jet.server.network.packet.packets.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.LoginTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientLoginRequestLoginPacket a client login request login packet}.
 *
 * @author Codestech
 * @see ClientLoginRequestLoginPacket
 * @see ClientPacketHandler
 * @since 1.0
 */
public final class ClientLoginRequestLoginPacketHandler extends ClientPacketHandler<ClientLoginRequestLoginPacket> {
    /**
     * Constructs the {@linkplain ClientLoginRequestLoginPacketHandler client login request login packet handler}.
     *
     * @since 1.0
     */
    public ClientLoginRequestLoginPacketHandler() {
        super(ClientLoginRequestLoginPacket.class);
    }

    @Override
    public void handle(@NonNull ClientLoginRequestLoginPacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof LoginTask loginTask))
            throw new IllegalArgumentException("The current session task is not a login session task");
        loginTask.handleLoginRequest(packet);
    }
}