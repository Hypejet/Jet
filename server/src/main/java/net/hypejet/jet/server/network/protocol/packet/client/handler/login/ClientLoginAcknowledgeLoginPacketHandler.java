package net.hypejet.jet.server.network.protocol.packet.client.handler.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginAcknowledgeLoginPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.LoginTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientLoginAcknowledgeLoginPacket a login acknowledge login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginAcknowledgeLoginPacket
 * @see ClientPacketHandler
 */
public final class ClientLoginAcknowledgeLoginPacketHandler
        implements ClientPacketHandler<ClientLoginAcknowledgeLoginPacket> {
    @Override
    public @NonNull ClientLoginAcknowledgeLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientLoginAcknowledgeLoginPacket();
    }

    @Override
    public void handle(@NonNull ClientLoginAcknowledgeLoginPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof LoginTask loginTask))
            throw new IllegalArgumentException("The current session task is not a login task");
        // TODO: Call an event?
        loginTask.acknowledgeFinishLogin();
    }
}