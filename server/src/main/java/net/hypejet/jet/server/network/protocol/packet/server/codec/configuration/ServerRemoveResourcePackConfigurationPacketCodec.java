package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRemoveResourcePackConfigurationPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerRemoveResourcePackConfigurationPacket remove resource pack configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerRemoveResourcePackConfigurationPacket
 * @see PacketCodec
 */
public final class ServerRemoveResourcePackConfigurationPacketCodec
        extends PacketCodec<ServerRemoveResourcePackConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerRemoveResourcePackConfigurationPacketCodec remove resource pack configuration
     * packet codec}.
     *
     * @since 1.0
     */
    public ServerRemoveResourcePackConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_REMOVE_RESOURCE_PACK,
                ServerRemoveResourcePackConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerRemoveResourcePackConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerRemoveResourcePackConfigurationPacket(
                buf.readBoolean() ? NetworkUtil.readUniqueId(buf) : null
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerRemoveResourcePackConfigurationPacket object) {
        UUID uniqueId = object.uniqueId();
        buf.writeBoolean(uniqueId != null);

        if (uniqueId != null) {
            NetworkUtil.writeUniqueId(buf, uniqueId);
        }
    }
}