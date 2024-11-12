package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientInformationConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.settings.PlayerSettingsCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientInformationConfigurationPacket client information configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientInformationConfigurationPacket
 * @see ClientPacketCodec
 */
public final class ClientInformationConfigurationPacketCodec
        extends ClientPacketCodec<ClientInformationConfigurationPacket> {
    /**
     * Constructs a {@linkplain ClientInformationConfigurationPacketCodec client information configuration packet
     * codec}.
     *
     * @since 1.0
     */
    public ClientInformationConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_CLIENT_INFORMATION, ClientInformationConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientInformationConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientInformationConfigurationPacket(PlayerSettingsCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientInformationConfigurationPacket object) {
        PlayerSettingsCodec.instance().write(buf, object.settings());
    }

    @Override
    public void handle(@NonNull ClientInformationConfigurationPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof ConfigurationTask configurationTask))
            throw new IllegalArgumentException("The session task must be a configuration task");
        configurationTask.player().settings(packet.settings());
    }
}