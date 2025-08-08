package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientResourcePackStatePacket;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.resource.ResourcePackStatus;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

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

    private static final IndexNetworkCodec<ResourcePackStatus, Integer> STATE_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, ResourcePackStatus.SUCCESSFULLY_LOADED,
                    1, ResourcePackStatus.DECLINED,
                    2, ResourcePackStatus.FAILED_DOWNLOAD,
                    3, ResourcePackStatus.ACCEPTED,
                    4, ResourcePackStatus.DOWNLOADED,
                    5, ResourcePackStatus.INVALID_URL,
                    6, ResourcePackStatus.FAILED_RELOAD,
                    7, ResourcePackStatus.DISCARDED
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private ClientResourcePackStatePacketReader() {}

    @Override
    public @NonNull ClientResourcePackStatePacket read(@NonNull ByteBuf buf) {
        return new ClientResourcePackStatePacket(UUIDNetworkCodec.INSTANCE.read(buf), STATE_CODEC.read(buf));
    }
}