package net.hypejet.jet.server.network.packet.client.handler.common;

import net.hypejet.jet.network.packet.client.common.ClientKeepAlivePacket;
import net.hypejet.jet.server.network.packet.client.handler.ClientPacketHandler;
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
        // TODO: Print a warning when the session task is not a keep alive response handler?
        if (!(session.sessionTask() instanceof KeepAliveResponseHandler responseHandler)) return;
        responseHandler.handleKeepAliveResponse(packet.keepAliveIdentifier());
    }
}