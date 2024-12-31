package net.hypejet.jet.server.network.packet.handler.handshake;

import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.HandshakeSessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientHandshakePacket a client handshake packet}.
 *
 * @since 1.0
 * @see ClientHandshakePacket
 * @see ClientPacketHandler
 */
public final class HandshakePacketHandler extends ClientPacketHandler<ClientHandshakePacket> {
    /**
     * Constructs the {@linkplain HandshakePacketHandler client handshake packet handler}.
     *
     * @since 1.0
     */
    public HandshakePacketHandler() {
        super(ClientHandshakePacket.class);
    }

    @Override
    public void handle(@NonNull ClientHandshakePacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof HandshakeSessionTask task))
            throw new IllegalArgumentException("The current session task is not a handshaking session task");
        task.handleHandshakePacket(packet);
    }
}