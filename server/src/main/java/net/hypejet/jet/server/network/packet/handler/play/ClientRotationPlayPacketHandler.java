package net.hypejet.jet.server.network.packet.handler.play;

import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientRotationPlayPacket;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientRotationPlayPacket a client rotation play packet}.
 *
 * @see ClientRotationPlayPacket
 * @see ClientPacketHandler
 * @since 1.0
 */
public final class ClientRotationPlayPacketHandler extends ClientPacketHandler<ClientRotationPlayPacket> {
    /**
     * Constructs the {@linkplain ClientRotationPlayPacketHandler client rotation play packet handler}.
     *
     * @since 1.0
     */
    public ClientRotationPlayPacketHandler() {
        super(ClientRotationPlayPacket.class);
    }

    @Override
    public void handle(@NonNull ClientRotationPlayPacket packet, @NonNull Session session) {
        JetPlayer player = session.connection().playerOrThrow();
        player.movementHandler().handleClientMovement(position -> position.withView(packet.yaw(), packet.pitch()));
        // TODO: Handle flags
    }
}