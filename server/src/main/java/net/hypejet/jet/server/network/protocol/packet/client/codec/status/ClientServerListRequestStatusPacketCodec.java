package net.hypejet.jet.server.network.protocol.packet.client.codec.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientServerListRequestStatusPacket server list request status packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientServerListRequestStatusPacket
 * @see PacketCodec
 */
public final class ClientServerListRequestStatusPacketCodec extends PacketCodec<ClientServerListRequestStatusPacket> {
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
        // Empty
    }
}
