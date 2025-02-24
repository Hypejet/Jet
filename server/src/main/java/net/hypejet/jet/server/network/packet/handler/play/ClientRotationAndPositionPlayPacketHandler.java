package net.hypejet.jet.server.network.packet.handler.play;

import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientRotationAndPositionPlayPacket;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientRotationAndPositionPlayPacket a client rotation and position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientRotationAndPositionPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientRotationAndPositionPlayPacketHandler
        extends ClientPacketHandler<ClientRotationAndPositionPlayPacket> {
    /**
     * Constructs the {@linkplain ClientRotationAndPositionPlayPacketHandler client rotation and position play packet
     * handler}.
     *
     * @since 1.0
     */
    public ClientRotationAndPositionPlayPacketHandler() {
        super(ClientRotationAndPositionPlayPacket.class);
    }

    @Override
    public void handle(@NonNull ClientRotationAndPositionPlayPacket packet, @NonNull Session session) {
        JetPlayer player = session.connection().playerOrThrow();
        player.movementHandler().handleClientMovement(position -> packet.position());
        // TODO: Handle flags
    }
}