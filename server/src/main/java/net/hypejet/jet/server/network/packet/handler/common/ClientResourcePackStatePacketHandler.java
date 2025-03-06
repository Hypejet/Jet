package net.hypejet.jet.server.network.packet.handler.common;

import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientResourcePackStatePacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.common.CommonSessionPacketHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientResourcePackStatePacket a client resource pack state packet}.
 *
 * @since 1.0
 * @see ClientResourcePackStatePacket
 * @see ClientPacketHandler
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
        if (!(session.sessionTask() instanceof CommonSessionPacketHandler handler))
            throw new IllegalArgumentException("The session task does not implement a common session packet handler");
        handler.handleResourcePackStatus(packet.uniqueId(), packet.status());
    }
}