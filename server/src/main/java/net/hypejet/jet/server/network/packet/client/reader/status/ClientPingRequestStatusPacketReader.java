package net.hypejet.jet.server.network.packet.client.reader.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.status.ClientPingRequestStatusPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientPingRequestStatusPacket a client ping request status packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPingRequestStatusPacket
 * @see NetworkReader
 */
public final class ClientPingRequestStatusPacketReader implements NetworkReader<ClientPingRequestStatusPacket> {
    @Override
    public @NonNull ClientPingRequestStatusPacket read(@NonNull ByteBuf buf) {
        return new ClientPingRequestStatusPacket(buf.readLong());
    }
}
