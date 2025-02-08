package net.hypejet.jet.server.network.packet.handler.play;

import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientChunkBatchReceivedPlayPacket;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientChunkBatchReceivedPlayPacket a client chunk batch received play packet}.
 *
 * @since 1.0
 * @see ClientChunkBatchReceivedPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientChunkBatchReceivedPlayPacketHandler
        extends ClientPacketHandler<ClientChunkBatchReceivedPlayPacket> {
    /**
     * Constructs the {@linkplain ClientChunkBatchReceivedPlayPacketHandler client chunk batch received play packet
     * handler}.
     *
     * @since 1.0
     */
    public ClientChunkBatchReceivedPlayPacketHandler() {
        super(ClientChunkBatchReceivedPlayPacket.class);
    }

    @Override
    public void handle(@NonNull ClientChunkBatchReceivedPlayPacket packet, @NonNull Session session) {
        JetPlayer player = session.connection().playerOrThrow();
        player.chunkBatchHandler().handleChunkBatchReceived(packet.desiredChunksPerTick());
    }
}