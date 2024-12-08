package net.hypejet.jet.server.network.packet.handler.common;

import net.hypejet.jet.server.network.packet.packets.client.common.ClientKeepAlivePacket;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.keepalive.KeepAliveResponseHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientKeepAlivePacket a client keep alive packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientKeepAlivePacket
 * @see ClientPacketHandler
 */
public final class ClientKeepAlivePacketHandler extends ClientPacketHandler<ClientKeepAlivePacket> {
    /**
     * Constructs the {@linkplain ClientKeepAlivePacketHandler client keep alive packet handler}.
     *
     * @since 1.0
     */
    public ClientKeepAlivePacketHandler() {
        super(ClientKeepAlivePacket.class);
    }

    @Override
    public void handle(@NonNull ClientKeepAlivePacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof KeepAliveResponseHandler responseHandler)) {
            throw new IllegalStateException("A keep alive packet has been received in a session" +
                    ", which is not a keep alive response handler");
        }
        responseHandler.handleKeepAliveResponse(packet.keepAliveIdentifier());
    }
}