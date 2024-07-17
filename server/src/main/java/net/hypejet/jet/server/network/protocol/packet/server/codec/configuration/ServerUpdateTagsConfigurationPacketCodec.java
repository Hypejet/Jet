package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.TagRegistry;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.registry.tag.TagRegistryNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
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

    private static final CollectionNetworkCodec<TagRegistry> TAG_REGISTRY_COLLECTION_CODEC = CollectionNetworkCodec
            .create(TagRegistryNetworkCodec.instance());

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
        return new ServerUpdateTagsConfigurationPacket(TAG_REGISTRY_COLLECTION_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerUpdateTagsConfigurationPacket object) {
        TAG_REGISTRY_COLLECTION_CODEC.write(buf, object.registries());
    }
}