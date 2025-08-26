package net.hypejet.jet.server.network.packet.handler.play;

import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientPositionPlayPacket;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientPositionPlayPacket a client position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPositionPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientPositionPlayPacketHandler extends ClientPacketHandler<ClientPositionPlayPacket> {
    /**
     * Constructs the {@linkplain ClientPositionPlayPacketHandler client position play packet handler}.
     *
     * @since 1.0
     */
    public ClientPositionPlayPacketHandler() {
        super(ClientPositionPlayPacket.class);
    }

    @Override
    public void handle(@NonNull ClientPositionPlayPacket packet, @NonNull Session session) {
        JetPlayer player = session.connection().playerOrThrow();
        player.movementSynchronizer().handleClientMovement(position -> position.withValues(packet.vector()));
        // TODO: Handle flags
    }
}