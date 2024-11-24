package net.hypejet.jet.server.network.protocol.packet.client.handler.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import net.hypejet.jet.server.network.session.task.StatusSessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientServerListRequestStatusPacket a server list request status packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientServerListRequestStatusPacket
 * @see ClientPacketHandler
 */
public final class ClientServerListRequestStatusPacketHandler
        implements ClientPacketHandler<ClientServerListRequestStatusPacket> {
    @Override
    public @NonNull ClientServerListRequestStatusPacket read(@NonNull ByteBuf buf) {
        return new ClientServerListRequestStatusPacket();
    }

    @Override
    public void handle(@NonNull ClientServerListRequestStatusPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof StatusSessionTask task))
            throw new IllegalArgumentException("The current session task must be a status session task");
        task.handleServerListRequest();
    }
}
