package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientResourcePackStatePacket;
import net.kyori.adventure.resource.ResourcePackStatus;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientResourcePackStatePacket a client resource pack state packet}.
 *
 * @since 1.0
 * @see ClientResourcePackStatePacket
 * @see NetworkReader
 */
public final class ClientResourcePackStatePacketReader implements NetworkReader<ClientResourcePackStatePacket> {

    /**
     * An instance of the {@linkplain ClientResourcePackStatePacketReader resource packet state packet reader}.
     *
     * @since 1.0
     */
    public static final ClientResourcePackStatePacketReader INSTANCE = new ClientResourcePackStatePacketReader();

    private static final MapperNetworkCodec<ResourcePackStatus, Integer> STATE_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(ResourcePackStatus.class, int.class)
                    .register(ResourcePackStatus.SUCCESSFULLY_LOADED, 0)
                    .register(ResourcePackStatus.DECLINED, 1)
                    .register(ResourcePackStatus.FAILED_DOWNLOAD, 2)
                    .register(ResourcePackStatus.ACCEPTED, 3)
                    .register(ResourcePackStatus.DOWNLOADED, 4)
                    .register(ResourcePackStatus.INVALID_URL, 5)
                    .register(ResourcePackStatus.FAILED_RELOAD, 6)
                    .register(ResourcePackStatus.DISCARDED, 7)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    private ClientResourcePackStatePacketReader() {}

    @Override
    public @NonNull ClientResourcePackStatePacket read(@NonNull ByteBuf buf) {
        return new ClientResourcePackStatePacket(UUIDNetworkCodec.INSTANCE.read(buf), STATE_CODEC.read(buf));
    }
}