package net.hypejet.jet.server.network.packet.client.handler.common;

import net.hypejet.jet.event.events.player.PlayerCookieResponseEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.network.packet.client.common.ClientCookieResponsePacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.client.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
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
        JetPlayer player = session.connection().player();
        if (player == null) return; // TODO: Handle the packet for connections during the login state

        EventNode<Object> eventNode = player.server().eventNode();
        UnmodifiableByteArray data = packet.data();
        eventNode.call(new PlayerCookieResponseEvent(player, packet.key(), data == null ? null : data.array()));
    }
}