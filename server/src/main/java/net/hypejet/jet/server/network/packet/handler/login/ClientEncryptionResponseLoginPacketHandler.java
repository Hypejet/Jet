package net.hypejet.jet.server.network.packet.handler.login;

import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.login.ClientEncryptionResponseLoginPacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.LoginSessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientEncryptionResponseLoginPacket a client encryption response login packet}.
 *
 * @since 1.0
 * @see ClientEncryptionResponseLoginPacket
 * @see ClientPacketHandler
 */
public final class ClientEncryptionResponseLoginPacketHandler
        extends ClientPacketHandler<ClientEncryptionResponseLoginPacket> {
    /**
     * Constructs the {@linkplain ClientEncryptionResponseLoginPacketHandler client encryption response login packet
     * handler}.
     *
     * @since 1.0
     */
    public ClientEncryptionResponseLoginPacketHandler() {
        super(ClientEncryptionResponseLoginPacket.class);
    }

    @Override
    public void handle(@NonNull ClientEncryptionResponseLoginPacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof LoginSessionTask))
            throw new IllegalArgumentException("The current session task is not a login session task");
        // TODO
    }
}