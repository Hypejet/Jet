package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.signing.SeenMessagesNetworkReader;
import net.hypejet.jet.server.network.codec.game.signing.SignedArgumentNetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientSignedChatCommandPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientSignedChatCommandPlayPacket a signed chat command play packet}.
 *
 * @since 1.0
 * @see ClientSignedChatCommandPlayPacket
 * @see NetworkReader
 */
public final class ClientSignedChatCommandPlayPacketReader
        implements NetworkReader<ClientSignedChatCommandPlayPacket> {

    /**
     * An instance of the {@linkplain ClientSignedChatCommandPlayPacketReader client signed chat command play packet
     * reader}.
     *
     * @since 1.0
     */
    public static final ClientSignedChatCommandPlayPacketReader
            INSTANCE = new ClientSignedChatCommandPlayPacketReader();

    private ClientSignedChatCommandPlayPacketReader() {}

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