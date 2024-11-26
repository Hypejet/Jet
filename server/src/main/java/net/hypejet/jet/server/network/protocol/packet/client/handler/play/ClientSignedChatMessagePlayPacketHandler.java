package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientSignedChatMessagePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.array.bytes.ByteArrayNetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.game.signing.SeenMessagesNetworkReader;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handles}, which reads and handles
 * {@linkplain ClientSignedChatMessagePlayPacket a signed chat message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientSignedChatMessagePlayPacket
 * @see ClientPacketHandler
 */
public final class ClientSignedChatMessagePlayPacketHandler
        implements ClientPacketHandler<ClientSignedChatMessagePlayPacket> {

    private static final StringNetworkCodec MESSAGE_CODEC = StringNetworkCodec.create(256);
    private static final ByteArrayNetworkReader SIGNATURE_READER = new ByteArrayNetworkReader(256);

    @Override
    public @NonNull ClientSignedChatMessagePlayPacket read(@NonNull ByteBuf buf) {
        return new ClientSignedChatMessagePlayPacket(
                MESSAGE_CODEC.read(buf), buf.readLong(),
                buf.readLong(), NetworkUtil.readOptional(SIGNATURE_READER, buf),
                SeenMessagesNetworkReader.INSTANCE.read(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientSignedChatMessagePlayPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}