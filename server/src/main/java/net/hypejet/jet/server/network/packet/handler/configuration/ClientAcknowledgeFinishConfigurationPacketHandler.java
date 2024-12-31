package net.hypejet.jet.server.network.packet.handler.configuration;

import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.packet.packets.client.configuration.ClientAcknowledgeFinishConfigurationPacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.ConfigurationSessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientAcknowledgeFinishConfigurationPacket a client acknowledge finish configuration packet}.
 *
 * @since 1.0
 * @see ClientAcknowledgeFinishConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientAcknowledgeFinishConfigurationPacketHandler
        extends ClientPacketHandler<ClientAcknowledgeFinishConfigurationPacket> {
    /**
     * Constructs the {@linkplain ClientAcknowledgeFinishConfigurationPacketHandler client acknowledge finish
     * configuration packet handler}.
     *
     * @since 1.0
     */
    public ClientAcknowledgeFinishConfigurationPacketHandler() {
        super(ClientAcknowledgeFinishConfigurationPacket.class);
    }

    @Override
    public void handle(@NonNull ClientAcknowledgeFinishConfigurationPacket packet, @NonNull Session session) {
        if (!(session.sessionTask() instanceof ConfigurationSessionTask task))
            throw new IllegalArgumentException("The current session task is not a configuration session task");
        task.handleFinishAcknowledge();
    }
}