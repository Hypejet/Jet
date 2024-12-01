package net.hypejet.jet.server.network.packet.client.handler.common;

import net.hypejet.jet.event.events.player.PlayerPongEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.network.packet.client.common.ClientPongPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.client.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientPongPacket a client pong packet}.
 *
 * @author Codestech
 * @see ClientPongPacket
 * @see ClientPacketHandler
 * @since 1.0
 */
public final class ClientPongPacketHandler extends ClientPacketHandler<ClientPongPacket> {
    /**
     * Constructs the {@linkplain ClientPongPacketHandler client pong packet handler}.
     *
     * @since 1.0
     */
    public ClientPongPacketHandler() {
        super(ClientPongPacket.class);
    }

    @Override
    public void handle(@NonNull ClientPongPacket packet, @NonNull Session session){
        JetPlayer player = session.connection().playerOrThrow();
        EventNode<Object> eventNode = player.server().eventNode();
        eventNode.call(new PlayerPongEvent(player, packet.pingIdentifier()));
    }
}