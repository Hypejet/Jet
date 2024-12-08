package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientResourcePackStatePacket;
import net.hypejet.jet.pack.ResourcePackState;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientResourcePackStatePacket a client resource pack state packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientResourcePackStatePacket
 * @see NetworkReader
 */
public final class ClientResourcePackStatePacketReader implements NetworkReader<ClientResourcePackStatePacket> {

    private static final MapperNetworkCodec<ResourcePackState, Integer> STATE_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(ResourcePackState.class, int.class)
                    .register(ResourcePackState.SUCCESS, 0)
                    .register(ResourcePackState.DECLINED, 1)
                    .register(ResourcePackState.FAILED_TO_DOWNLOAD, 2)
                    .register(ResourcePackState.ACCEPTED, 3)
                    .register(ResourcePackState.DOWNLOADED, 4)
                    .register(ResourcePackState.INVALID_URL, 5)
                    .register(ResourcePackState.FAILED_TO_RELOAD, 6)
                    .register(ResourcePackState.DISCARDED, 7)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    @Override
    public @NonNull ClientResourcePackStatePacket read(@NonNull ByteBuf buf) {
        return new ClientResourcePackStatePacket(UUIDNetworkCodec.INSTANCE.read(buf), STATE_CODEC.read(buf));
    }
}