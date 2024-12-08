package net.hypejet.jet.server.network.packet.handler.login;

import net.hypejet.jet.server.network.packet.packets.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.LoginTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientLoginAcknowledgeLoginPacket a client login acknowledge login packet}.
 *
 * @author Codestech
 * @see ClientLoginAcknowledgeLoginPacket
 * @see ClientPacketHandler
 * @since 1.0
 */
public final class ClientLoginAcknowledgeLoginPacketHandler
        extends ClientPacketHandler<ClientLoginAcknowledgeLoginPacket> {
    /**
     * Constructs the {@linkplain ClientLoginAcknowledgeLoginPacketHandler client login acknowledge login packet
     * handler}.
     *
     * @since 1.0
     */
    public ClientLoginAcknowledgeLoginPacketHandler() {
        super(ClientLoginAcknowledgeLoginPacket.class);
    }

    @Override
    public void handle(@NonNull ClientLoginAcknowledgeLoginPacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof LoginTask loginTask))
            throw new IllegalArgumentException("The current session task is not a login session task");
        loginTask.acknowledgeFinishLogin();
    }
}