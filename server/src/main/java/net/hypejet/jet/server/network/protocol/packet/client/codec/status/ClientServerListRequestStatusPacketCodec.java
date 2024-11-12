package net.hypejet.jet.server.network.protocol.packet.client.codec.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.session.task.SessionTask;
import net.hypejet.jet.server.network.session.task.StatusSessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientServerListRequestStatusPacket server list request status packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientServerListRequestStatusPacket
 * @see ClientPacketCodec
 */
public final class ClientServerListRequestStatusPacketCodec
        extends ClientPacketCodec<ClientServerListRequestStatusPacket> {
    /**
     * Constructs the {@linkplain ClientServerListRequestStatusPacketCodec server list request status packet codec}.
     *
     * @since 1.0
     */
    public ClientServerListRequestStatusPacketCodec() {
        super(ClientPacketIdentifiers.STATUS_SERVER_LIST_REQUEST, ClientServerListRequestStatusPacket.class);
    }

    @Override
    public @NonNull ClientServerListRequestStatusPacket read(@NonNull ByteBuf buf) {
        return new ClientServerListRequestStatusPacket();
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientServerListRequestStatusPacket object) {
        // NOOP
    }

    @Override
    public void handle(@NonNull ClientServerListRequestStatusPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof StatusSessionTask task))
            throw new IllegalArgumentException("The current session task must be a status session task");
        task.handleServerListRequest();
    }
}
