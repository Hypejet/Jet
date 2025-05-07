package net.hypejet.jet.server.network.packet.handler.common;

import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientKeepAlivePacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.common.CommonSessionPacketHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientKeepAlivePacket a client keep alive packet}.
 *
 * @since 1.0
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
        if (!(session.sessionTask() instanceof CommonSessionPacketHandler handler))
            throw new IllegalArgumentException("The session task does not implement a common session packet handler");
        handler.handleKeepAliveResponse(packet.keepAliveIdentifier());
    }
}