package net.hypejet.jet.server.network.packet.client.handler.common;

import net.hypejet.jet.event.events.cookie.CookieResponseEvent;
import net.hypejet.jet.network.packet.client.common.ClientCookieResponsePacket;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.client.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which
 * handles {@linkplain ClientCookieResponsePacket a client cookie response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCookieResponsePacket
 * @see ClientPacketHandler
 */
public final class ClientCookieResponsePacketHandler extends ClientPacketHandler<ClientCookieResponsePacket> {
    /**
     * Constructs the {@linkplain ClientCookieResponsePacketHandler client cookie response packet handler}.
     *
     * @since 1.0
     */
    public ClientCookieResponsePacketHandler() {
        super(ClientCookieResponsePacket.class);
    }

    @Override
    public void handle(@NonNull ClientCookieResponsePacket packet, @NonNull Session session) {
        SocketPlayerConnection connection = session.connection();
        connection.server().eventNode().call(new CookieResponseEvent(connection, packet.key(), packet.data()));
    }
}