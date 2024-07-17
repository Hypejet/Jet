package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerPluginMessageConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.identifier.PackedIdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerPluginMessageConfigurationPacket plugin message configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageConfigurationPacket
 * @see PacketCodec
 */
public final class ServerPluginMessageConfigurationPacketCodec
        extends PacketCodec<ServerPluginMessageConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerPluginMessageConfigurationPacketCodec plugin message configuration packet
     * codec}.
     *
     * @since 1.0
     */
    public ServerPluginMessageConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_PLUGIN_MESSAGE, ServerPluginMessageConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerPluginMessageConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerPluginMessageConfigurationPacket(PackedIdentifierNetworkCodec.instance().read(buf),
                NetworkUtil.readRemainingBytes(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPluginMessageConfigurationPacket object) {
        PackedIdentifierNetworkCodec.instance().write(buf, object.identifier());
        buf.writeBytes(object.data());
    }
}
