package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientSignedChatCommandPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.signing.SeenMessagesNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.signing.SignedArgumentNetworkReader;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientSignedChatCommandPlayPacket a signed chat command play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientSignedChatCommandPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientSignedChatCommandPlayPacketHandler
        implements ClientPacketHandler<ClientSignedChatCommandPlayPacket> {
    @Override
    public @NonNull ClientSignedChatCommandPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientSignedChatCommandPlayPacket(
                StringNetworkCodec.instance().read(buf),
                buf.readLong(), buf.readLong(),
                SignedArgumentNetworkReader.COLLECTION_READER.read(buf),
                SeenMessagesNetworkCodec.instance().read(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientSignedChatCommandPlayPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}