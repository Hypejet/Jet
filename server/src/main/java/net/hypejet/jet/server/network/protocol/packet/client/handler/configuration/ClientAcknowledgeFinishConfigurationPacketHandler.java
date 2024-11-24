package net.hypejet.jet.server.network.protocol.packet.client.handler.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientAcknowledgeFinishConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientAcknowledgeFinishConfigurationPacket a client acknowledge finish configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientAcknowledgeFinishConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientAcknowledgeFinishConfigurationPacketHandler
        implements ClientPacketHandler<ClientAcknowledgeFinishConfigurationPacket> {

    private static final ClientAcknowledgeFinishConfigurationPacket
            PACKET = new ClientAcknowledgeFinishConfigurationPacket();

    @Override
    public @NonNull ClientAcknowledgeFinishConfigurationPacket read(@NonNull ByteBuf buf) {
        return PACKET;
    }

    @Override
    public void handle(@NonNull ClientAcknowledgeFinishConfigurationPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof ConfigurationTask configurationTask))
            throw new IllegalArgumentException("The session task is must be a configuration task");
        configurationTask.handleFinishAcknowledge();
    }
}