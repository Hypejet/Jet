package net.hypejet.jet.server.network.packet.client.handler.common;

import net.hypejet.jet.network.packet.client.common.ClientInformationPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.client.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which
 * handles {@linkplain ClientInformationPacket a client information packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientInformationPacket
 * @see ClientPacketHandler
 */
public final class ClientInformationPacketHandler extends ClientPacketHandler<ClientInformationPacket> {
    /**
     * Constructs the {@linkplain ClientInformationPacketHandler client information packet handler}.
     *
     * @since 1.0
     */
    public ClientInformationPacketHandler() {
        super(ClientInformationPacket.class);
    }

    @Override
    public void handle(@NonNull ClientInformationPacket packet, @NonNull Session session) {
        JetPlayer player = session.connection().player();
        if (player == null) return;
        player.settings(packet.settings());
    }
}