package net.hypejet.jet.server.network.packet.handler.play;

import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientChatCommandPlayPacket;
import net.hypejet.jet.server.network.session.Session;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientChatCommandPlayPacket a client chat command play packet}.
 *
 * @since 1.0
 * @see ClientChatCommandPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientChatCommandPlayPacketHandler extends ClientPacketHandler<ClientChatCommandPlayPacket> {
    /**
     * Constructs the {@linkplain ClientChatCommandPlayPacketHandler client chat command play packet handler}.
     *
     * @since 1.0
     */
    public ClientChatCommandPlayPacketHandler() {
        super(ClientChatCommandPlayPacket.class);
    }

    @Override
    public void handle(@NonNull ClientChatCommandPlayPacket packet, @NonNull Session session) {
        JetPlayer player = session.connection().playerOrThrow();
        player.server().commandManager().execute(packet.commandString(), player);
    }
}