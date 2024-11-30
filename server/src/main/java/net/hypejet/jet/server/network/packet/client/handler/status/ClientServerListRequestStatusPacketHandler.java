package net.hypejet.jet.server.network.packet.client.handler.status;

import net.hypejet.jet.network.packet.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.server.network.packet.client.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.StatusSessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientServerListRequestStatusPacket a client server list request status packet}.
 *
 * @author Codestech
 * @see ClientServerListRequestStatusPacket
 * @see ClientPacketHandler
 * @since 1.0
 */
public final class ClientServerListRequestStatusPacketHandler
        extends ClientPacketHandler<ClientServerListRequestStatusPacket> {
    /**
     * Constructs the {@linkplain ClientServerListRequestStatusPacketHandler client server list request status packet
     * handler}.
     *
     * @since 1.0
     */
    public ClientServerListRequestStatusPacketHandler() {
        super(ClientServerListRequestStatusPacket.class);
    }

    @Override
    public void handle(@NonNull ClientServerListRequestStatusPacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof StatusSessionTask task))
            throw new IllegalArgumentException("The current session task is not a status session task");
        task.handleServerListRequest();
    }
}