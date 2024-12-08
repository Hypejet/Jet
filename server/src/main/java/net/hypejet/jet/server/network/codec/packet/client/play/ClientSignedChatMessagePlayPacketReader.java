package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientSignedChatMessagePlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.ByteArrayNetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.codec.game.signing.SeenMessagesNetworkReader;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientSignedChatMessagePlayPacket a signed chat message play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientSignedChatMessagePlayPacket
 * @see NetworkReader
 */
public final class ClientSignedChatMessagePlayPacketReader
        implements NetworkReader<ClientSignedChatMessagePlayPacket> {

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
}