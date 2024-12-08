package net.hypejet.jet.server.network.codec.packet.client.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientServerListRequestStatusPacket a server list request status packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientServerListRequestStatusPacket
 * @see NetworkReader
 */
public final class ClientServerListRequestStatusPacketReader
        implements NetworkReader<ClientServerListRequestStatusPacket> {
    @Override
    public @NonNull ClientServerListRequestStatusPacket read(@NonNull ByteBuf buf) {
        return new ClientServerListRequestStatusPacket();
    }
}
