package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientInformationConfigurationPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.codecs.settings.PlayerSettingsCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

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
    public void handle(@NonNull ClientInformationConfigurationPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        JetPlayer player = Objects.requireNonNull(connection.player(), "The player cannot be null");
        player.settings(packet.settings());
    }
}