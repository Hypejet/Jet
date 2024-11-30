package net.hypejet.jet.server.network.packet.client.handler.status;

import net.hypejet.jet.network.packet.client.status.ClientPingRequestStatusPacket;
import net.hypejet.jet.server.network.packet.client.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.StatusSessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientPingRequestStatusPacket a client ping request status packet}.
 *
 * @author Codestech
 * @see ClientPingRequestStatusPacket
 * @see ClientPacketHandler
 * @since 1.0
 */
public final class ClientPingRequestStatusPacketHandler extends ClientPacketHandler<ClientPingRequestStatusPacket> {
    /**
     * Constructs the {@linkplain ClientPingRequestStatusPacketHandler client ping request status packet handler}.
     *
     * @since 1.0
     */
    public ClientPingRequestStatusPacketHandler() {
        super(ClientPingRequestStatusPacket.class);
    }

    @Override
    public void handle(@NonNull ClientPingRequestStatusPacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof StatusSessionTask task))
            throw new IllegalArgumentException("The current session is not a status session task");
        task.handlePingRequest(packet);
    }
}