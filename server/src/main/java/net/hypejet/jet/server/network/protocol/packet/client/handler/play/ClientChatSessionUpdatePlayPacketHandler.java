package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientChatSessionUpdatePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.array.bytes.ByteArrayNetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientChatSessionUpdatePlayPacket a chat session update play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientChatSessionUpdatePlayPacket
 * @see ClientPacketHandler
 */
public final class ClientChatSessionUpdatePlayPacketHandler
        implements ClientPacketHandler<ClientChatSessionUpdatePlayPacket> {

    private static final ByteArrayNetworkReader PUBLIC_KEY_READER = new ByteArrayNetworkReader(512);
    private static final ByteArrayNetworkReader KEY_SIGNATURE_READER = new ByteArrayNetworkReader(4096);

    @Override
    public @NonNull ClientChatSessionUpdatePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientChatSessionUpdatePlayPacket(
                UUIDNetworkCodec.instance().read(buf), buf.readLong(),
                PUBLIC_KEY_READER.read(buf), KEY_SIGNATURE_READER.read(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientChatSessionUpdatePlayPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}