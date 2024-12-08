package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientChatCommandPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientChatCommandPlayPacket a chat
 * command play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientChatCommandPlayPacket
 * @see NetworkReader
 */
public final class ClientChatCommandPlayPacketReader implements NetworkReader<ClientChatCommandPlayPacket> {
    @Override
    public @NonNull ClientChatCommandPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientChatCommandPlayPacket(StringNetworkCodec.INSTANCE.read(buf));
    }
}