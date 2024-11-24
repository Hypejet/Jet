package net.hypejet.jet.server.network.protocol.packet.client.handler.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPongConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientPongConfigurationPacket a pong configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPongConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientPongConfigurationPacketHandler
        implements ClientPacketHandler<ClientPongConfigurationPacket> {
    @Override
    public @NonNull ClientPongConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientPongConfigurationPacket(buf.readInt());
    }

    @Override
    public void handle(@NonNull ClientPongConfigurationPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof ConfigurationTask configurationTask))
            throw new IllegalArgumentException("The session task must be a configuration task");
        configurationTask.player().handlePong(packet.pingIdentifier());
    }
}