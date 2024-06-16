package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPluginMessageConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientPluginMessageConfigurationPacket plugin message configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPluginMessageConfigurationPacket
 * @see PacketCodec
 */
public final class ClientPluginMessageConfigurationPacketCodec
        extends PacketCodec<ClientPluginMessageConfigurationPacket> {
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
}
