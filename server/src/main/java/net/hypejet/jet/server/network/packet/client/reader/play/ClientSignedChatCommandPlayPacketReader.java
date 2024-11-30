package net.hypejet.jet.server.network.packet.client.reader.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.play.ClientSignedChatCommandPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.codec.game.signing.SeenMessagesNetworkReader;
import net.hypejet.jet.server.network.codec.game.signing.SignedArgumentNetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientSignedChatCommandPlayPacket a signed chat command play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientSignedChatCommandPlayPacket
 * @see NetworkReader
 */
public final class ClientSignedChatCommandPlayPacketReader
        implements NetworkReader<ClientSignedChatCommandPlayPacket> {
    @Override
    public @NonNull ClientSignedChatCommandPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientSignedChatCommandPlayPacket(
                StringNetworkCodec.INSTANCE.read(buf),
                buf.readLong(), buf.readLong(),
                SignedArgumentNetworkReader.COLLECTION_READER.read(buf),
                SeenMessagesNetworkReader.INSTANCE.read(buf)
        );
    }
}