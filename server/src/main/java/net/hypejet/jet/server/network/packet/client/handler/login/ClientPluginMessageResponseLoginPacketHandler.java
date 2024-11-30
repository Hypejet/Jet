package net.hypejet.jet.server.network.packet.client.handler.login;

import net.hypejet.jet.network.packet.client.login.ClientPluginMessageResponseLoginPacket;
import net.hypejet.jet.server.network.packet.client.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.LoginTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientPluginMessageResponseLoginPacket a client plugin message response login packet}.
 *
 * @author Codestech
 * @see ClientPluginMessageResponseLoginPacket
 * @see ClientPacketHandler
 * @since 1.0
 */
public final class ClientPluginMessageResponseLoginPacketHandler
        extends ClientPacketHandler<ClientPluginMessageResponseLoginPacket> {
    /**
     * Constructs the {@linkplain ClientPluginMessageResponseLoginPacketHandler client plugin message response login
     * packet handler}.
     *
     * @since 1.0
     */
    public ClientPluginMessageResponseLoginPacketHandler() {
        super(ClientPluginMessageResponseLoginPacket.class);
    }

    @Override
    public void handle(@NonNull ClientPluginMessageResponseLoginPacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof LoginTask loginTask))
            throw new IllegalArgumentException("The current session task is not a login session task");
        // TODO: Call an event?
        loginTask.handlePacket(packet);
    }
}