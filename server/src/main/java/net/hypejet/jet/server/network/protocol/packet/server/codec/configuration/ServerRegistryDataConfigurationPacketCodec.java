package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRegistryDataConfigurationPacket.Entry;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.IdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.registry.RegistryDataEntryNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerRegistryDataConfigurationPacket registry data configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerRegistryDataConfigurationPacket
 * @see PacketCodec
 */
public final class ServerRegistryDataConfigurationPacketCodec
        extends PacketCodec<ServerRegistryDataConfigurationPacket> {

    private static final CollectionNetworkCodec<Entry> REGISTRY_ENTRY_COLLECTION_CODEC = CollectionNetworkCodec
            .create(RegistryDataEntryNetworkCodec.instance());

    /**
     * Constructs the {@linkplain ServerRegistryDataConfigurationPacketCodec registry data configuration packet codec}.
     *
     * @since 1.0
     */
    public ServerRegistryDataConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_REGISTRY_DATA, ServerRegistryDataConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerRegistryDataConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerRegistryDataConfigurationPacket(IdentifierNetworkCodec.instance().read(buf),
                REGISTRY_ENTRY_COLLECTION_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerRegistryDataConfigurationPacket object) {
        IdentifierNetworkCodec.instance().write(buf, object.registry());
        REGISTRY_ENTRY_COLLECTION_CODEC.write(buf, object.entries());
    }
}