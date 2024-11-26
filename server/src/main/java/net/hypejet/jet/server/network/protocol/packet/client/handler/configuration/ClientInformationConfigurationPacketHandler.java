package net.hypejet.jet.server.network.protocol.packet.client.handler.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientInformationConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.game.settings.PlayerSettingsReader;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientInformationConfigurationPacket a client information configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientInformationConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientInformationConfigurationPacketHandler
        implements ClientPacketHandler<ClientInformationConfigurationPacket> {
    @Override
    public @NonNull ClientInformationConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientInformationConfigurationPacket(PlayerSettingsReader.INSTANCE.read(buf));
    }

    @Override
    public void handle(@NonNull ClientInformationConfigurationPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof ConfigurationTask configurationTask))
            throw new IllegalArgumentException("The session task must be a configuration task");
        configurationTask.player().settings(packet.settings());
    }
}