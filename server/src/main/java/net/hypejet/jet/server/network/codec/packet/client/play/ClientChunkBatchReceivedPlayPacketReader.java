package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientChunkBatchReceivedPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientChunkBatchReceivedPlayPacket a client chunk batch received play packet}.
 *
 * @since 1.0
 * @see ClientChunkBatchReceivedPlayPacket
 * @see NetworkReader
 */
public final class ClientChunkBatchReceivedPlayPacketReader
        implements NetworkReader<ClientChunkBatchReceivedPlayPacket> {
    /**
     * An instance of the {@linkplain ClientChunkBatchReceivedPlayPacketReader client chunk batch received play packet
     * reader}.
     *
     * @since 1.0
     */
    public static final ClientChunkBatchReceivedPlayPacketReader
            INSTANCE = new ClientChunkBatchReceivedPlayPacketReader();

    private ClientChunkBatchReceivedPlayPacketReader() {}

    @Override
    public @NonNull ClientChunkBatchReceivedPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientChunkBatchReceivedPlayPacket(buf.readFloat());
    }
}