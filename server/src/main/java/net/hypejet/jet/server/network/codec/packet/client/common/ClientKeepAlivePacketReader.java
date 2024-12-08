package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientKeepAlivePacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientKeepAlivePacket a client
 * keep alive packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientKeepAlivePacket
 * @see NetworkReader
 */
public final class ClientKeepAlivePacketReader implements NetworkReader<ClientKeepAlivePacket> {
    @Override
    public @NonNull ClientKeepAlivePacket read(@NonNull ByteBuf buf) {
        return new ClientKeepAlivePacket(buf.readLong());
    }
}