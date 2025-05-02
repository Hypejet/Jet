package net.hypejet.jet.server.network.packet.handler.common;

import net.hypejet.jet.event.events.brand.ChangeClientBrandEvent;
import net.hypejet.jet.event.events.pluginmessage.PluginMessageEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientPluginMessagePacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.common.CommonSessionPacketHandler;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
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

    private static final Key CLIENT_BRAND_PLUGIN_MESSAGE_KEY = Key.key("brand");

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
        if (!(session.sessionTask() instanceof CommonSessionPacketHandler handler))
            throw new IllegalArgumentException("The session task does not implement a common session packet handler");

        Key messageKey = packet.key();
        UnmodifiableByteArray data = packet.data();

        SocketPlayerConnection connection = session.connection();
        EventNode<Object> eventNode = connection.server().eventNode();

        if (messageKey.equals(CLIENT_BRAND_PLUGIN_MESSAGE_KEY)) {
            String clientBrand = new String(data.array(), StandardCharsets.UTF_8);
            eventNode.call(new ChangeClientBrandEvent(connection, clientBrand));
            handler.handleClientBrand(clientBrand);
        }

        eventNode.call(new PluginMessageEvent(connection, messageKey, data));
    }
}