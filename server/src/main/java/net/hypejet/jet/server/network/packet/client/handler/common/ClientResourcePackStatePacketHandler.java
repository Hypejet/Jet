package net.hypejet.jet.server.network.packet.client.handler.common;

import net.hypejet.jet.event.events.player.PlayerResourcePackResponseEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.network.packet.client.common.ClientResourcePackStatePacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.client.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientResourcePackStatePacket a client resource pack state packet}.
 *
 * @author Codestech
 * @see ClientResourcePackStatePacket
 * @see ClientPacketHandler
 * @since 1.0
 */
public final class ClientResourcePackStatePacketHandler extends ClientPacketHandler<ClientResourcePackStatePacket> {
    /**
     * Constructs the {@linkplain ClientResourcePackStatePacketHandler client resource pack state packet handler}.
     *
     * @since 1.0
     */
    public ClientResourcePackStatePacketHandler() {
        super(ClientResourcePackStatePacket.class);
    }

    @Override
    public void handle(@NonNull ClientResourcePackStatePacket packet, @NonNull Session session) {
        JetPlayer player = session.connection().player();
        if (player == null) return; // TODO: print a warning?
        EventNode<Object> eventNode = player.server().eventNode();
        eventNode.call(new PlayerResourcePackResponseEvent(player, packet.uniqueId(), packet.state()));
    }
}