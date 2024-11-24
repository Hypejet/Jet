package net.hypejet.jet.server.network.protocol.packet.client.handler.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.status.ClientPingRequestStatusPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import net.hypejet.jet.server.network.session.task.StatusSessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientPingRequestStatusPacket a client ping request status packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPingRequestStatusPacket
 * @see ClientPacketHandler
 */
public final class ClientPingRequestStatusPacketHandler implements ClientPacketHandler<ClientPingRequestStatusPacket> {
    @Override
    public @NonNull ClientPingRequestStatusPacket read(@NonNull ByteBuf buf) {
        return new ClientPingRequestStatusPacket(buf.readLong());
    }

    @Override
    public void handle(@NonNull ClientPingRequestStatusPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof StatusSessionTask task))
            throw new IllegalArgumentException("The current session task must be a status session task");
        task.handlePingRequest(packet);
    }
}
