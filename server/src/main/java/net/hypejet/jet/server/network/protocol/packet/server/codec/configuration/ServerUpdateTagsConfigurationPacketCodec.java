package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.tag.TagRegistryNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerUpdateTagsConfigurationPacket update tags configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerUpdateTagsConfigurationPacket
 * @see PacketCodec
 */
public final class ServerUpdateTagsConfigurationPacketCodec extends PacketCodec<ServerUpdateTagsConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerUpdateTagsConfigurationPacketCodec update tags configuration packet codec}.
     *
     * @since 1.0
     */
    public ServerUpdateTagsConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_UPDATE_TAGS, ServerUpdateTagsConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerUpdateTagsConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerUpdateTagsConfigurationPacket(
                NetworkUtil.readCollection(buf, TagRegistryNetworkCodec.instance())
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerUpdateTagsConfigurationPacket object) {
        NetworkUtil.writeCollection(buf, TagRegistryNetworkCodec.instance(), object.registries());
    }
}