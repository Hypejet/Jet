package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientPingRequestPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientPingRequestPacket a client
 * ping request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPingRequestPacket
 * @see NetworkReader
 */
public final class ClientPingRequestPacketReader implements NetworkReader<ClientPingRequestPacket> {
    @Override
    public @NonNull ClientPingRequestPacket read(@NonNull ByteBuf buf) {
        return new ClientPingRequestPacket(buf.readLong());
    }
}
