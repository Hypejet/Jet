package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPluginMessageConfigurationPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientPluginMessageConfigurationPacket plugin message configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPluginMessageConfigurationPacket
 * @see ClientPacketCodec
 */
public final class ClientPluginMessageConfigurationPacketCodec
        extends ClientPacketCodec<ClientPluginMessageConfigurationPacket> {
    /**
     * Constructs a {@linkplain ClientPluginMessageConfigurationPacketCodec plugin message configuration packet codec}.
     *
     * @since 1.0
     */
    public ClientPluginMessageConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_PLUGIN_MESSAGE, ClientPluginMessageConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientPluginMessageConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientPluginMessageConfigurationPacket(
                NetworkUtil.readIdentifier(buf),
                NetworkUtil.readRemainingBytes(buf)
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientPluginMessageConfigurationPacket object) {
        NetworkUtil.writeIdentifier(buf, object.identifier());
        buf.writeBytes(object.data());
    }

    @Override
    public void handle(@NonNull ClientPluginMessageConfigurationPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        JetPlayer player = Objects.requireNonNull(connection.player(), "Player cannot be null");
        player.handlePluginMessage(packet.identifier(), packet.data());
    }
}
