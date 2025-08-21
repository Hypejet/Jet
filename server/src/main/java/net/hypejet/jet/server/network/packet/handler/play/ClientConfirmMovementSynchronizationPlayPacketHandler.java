package net.hypejet.jet.server.network.packet.handler.play;

import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientConfirmMovementSynchronizationPlayPacket;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler} of
 * {@linkplain ClientConfirmMovementSynchronizationPlayPacket a client confirm movement synchronization play packet}.
 *
 * @since 1.0
 * @see ClientConfirmMovementSynchronizationPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientConfirmMovementSynchronizationPlayPacketHandler
        extends ClientPacketHandler<ClientConfirmMovementSynchronizationPlayPacket> {
    /**
     * Constructs the {@linkplain ClientConfirmMovementSynchronizationPlayPacketHandler client confirm movement
     * synchronization play packet handler}.
     *
     * @since 1.0
     */
    public ClientConfirmMovementSynchronizationPlayPacketHandler() {
        super(ClientConfirmMovementSynchronizationPlayPacket.class);
    }

    @Override
    public void handle(@NonNull ClientConfirmMovementSynchronizationPlayPacket packet, @NonNull Session session) {
        JetPlayer player = session.connection().playerOrThrow();
        player.movementSynchronizer().handleConfirmation(packet);
    }
}