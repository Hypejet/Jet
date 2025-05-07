package net.hypejet.jet.server.network.packet.handler.common;

import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientInformationPacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.common.CommonSessionPacketHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which
 * handles {@linkplain ClientInformationPacket a client information packet}.
 *
 * @since 1.0
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
        if (!(session.sessionTask() instanceof CommonSessionPacketHandler handler))
            throw new IllegalArgumentException("The session task does not implement a common session packet handler");
        handler.handleClientInformation(packet.settings());
    }
}