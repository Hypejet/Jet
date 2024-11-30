package net.hypejet.jet.server.network.packet.client.reader.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.common.ClientPongPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientPongPacket a client pong
 * packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPongPacket
 * @see NetworkReader
 */
public final class ClientPongPacketReader implements NetworkReader<ClientPongPacket> {
    @Override
    public @NonNull ClientPongPacket read(@NonNull ByteBuf buf) {
        return new ClientPongPacket(buf.readInt());
    }
}