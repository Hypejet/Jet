package net.hypejet.jet.server.network.protocol.packet.client.codec.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.status.ClientPingRequestStatusPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.session.JetStatusSession;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientPingRequestStatusPacket client ping request status packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPingRequestStatusPacket
 * @see ClientPacketCodec
 */
public final class ClientPingRequestStatusPacketCodec extends ClientPacketCodec<ClientPingRequestStatusPacket> {
    /**
     * Constructs the {@linkplain ClientPingRequestStatusPacketCodec ping request status packet codec}.
     *
     * @since 1.0
     */
    public ClientPingRequestStatusPacketCodec() {
        super(ClientPacketIdentifiers.STATUS_PING_REQUEST, ClientPingRequestStatusPacket.class);
    }

    @Override
    public @NonNull ClientPingRequestStatusPacket read(@NonNull ByteBuf buf) {
        return new ClientPingRequestStatusPacket(buf.readLong());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientPingRequestStatusPacket object) {
        buf.writeLong(object.payload());
    }

    @Override
    public void handle(@NonNull ClientPingRequestStatusPacket packet, @NonNull SocketPlayerConnection connection) {
        JetStatusSession session = JetStatusSession.asLoginSession(connection.getSession());
        session.handlePingRequest(packet);
    }
}
