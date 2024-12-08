package net.hypejet.jet.server.network.packet.handler.handshake;

import net.hypejet.jet.server.network.packet.packets.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.HandshakeTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientHandshakePacket a client handshake packet}.
 *
 * @author Codestech
 * @see ClientHandshakePacket
 * @see ClientPacketHandler
 * @since 1.0
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
        if (!(session.sessionTask() instanceof HandshakeTask task))
            throw new IllegalArgumentException("The current session task is not a handshaking task");
        task.handleHandshakePacket(packet);
    }
}