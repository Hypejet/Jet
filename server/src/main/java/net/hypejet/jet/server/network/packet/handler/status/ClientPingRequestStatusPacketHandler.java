package net.hypejet.jet.server.network.packet.handler.status;

import net.hypejet.jet.server.network.packet.packets.client.common.ClientPingRequestPacket;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.StatusSessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientPingRequestPacket a client ping request status packet}.
 *
 * @author Codestech
 * @see ClientPingRequestPacket
 * @see ClientPacketHandler
 * @since 1.0
 */
public final class ClientPingRequestStatusPacketHandler extends ClientPacketHandler<ClientPingRequestPacket> {
    /**
     * Constructs the {@linkplain ClientPingRequestStatusPacketHandler client ping request status packet handler}.
     *
     * @since 1.0
     */
    public ClientPingRequestStatusPacketHandler() {
        super(ClientPingRequestPacket.class);
    }

    @Override
    public void handle(@NonNull ClientPingRequestPacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof StatusSessionTask task))
            throw new IllegalArgumentException("The current session is not a status session task");
        task.handlePingRequest(packet);
    }
}