package net.hypejet.jet.server.network.packet.handler.common;

import net.hypejet.jet.event.events.brand.ChangeClientBrandEvent;
import net.hypejet.jet.event.events.pluginmessage.PluginMessageEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientPluginMessagePacket;
import net.hypejet.jet.server.network.session.Session;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.charset.StandardCharsets;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientPluginMessagePacket a client plugin message packet}.
 *
 * @since 1.0
 * @see ClientPluginMessagePacket
 * @see ClientPacketHandler
 */
public final class ClientPluginMessagePacketHandler extends ClientPacketHandler<ClientPluginMessagePacket> {

    private static final Key BRAND_PLUGIN_MESSAGE_IDENTIFIER = Key.key("brand");

    /**
     * Constructs the {@linkplain ClientPluginMessagePacketHandler client plugin message packet handler}.
     *
     * @since 1.0
     */
    public ClientPluginMessagePacketHandler() {
        super(ClientPluginMessagePacket.class);
    }

    @Override
    public void handle(@NonNull ClientPluginMessagePacket packet, @NonNull Session session) {
        JetPlayer player = session.connection().playerOrThrow();
        EventNode<Object> eventNode = player.server().eventNode();

        Key messageKey = packet.key();
        byte[] data = packet.data().array();

        if (messageKey.equals(BRAND_PLUGIN_MESSAGE_IDENTIFIER)) {
            String clientBrand = new String(data, StandardCharsets.UTF_8);
            eventNode.call(new ChangeClientBrandEvent(player, clientBrand));
            player.setClientBrand(clientBrand);
        }

        eventNode.call(new PluginMessageEvent(player, messageKey, data));
    }
}