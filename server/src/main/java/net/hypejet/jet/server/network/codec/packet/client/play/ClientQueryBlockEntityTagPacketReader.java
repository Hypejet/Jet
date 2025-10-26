package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.world.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientQueryBlockEntityTagPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientQueryBlockEntityTagPacket a query block entity tag packet}.
 *
 * @since 1.0
 * @see ClientQueryBlockEntityTagPacket
 * @see NetworkReader
 */
public final class ClientQueryBlockEntityTagPacketReader implements NetworkReader<ClientQueryBlockEntityTagPacket> {
    /**
     * An instance of the {@linkplain ClientQueryBlockEntityTagPacketReader client query block entity tag packet
     * reader}.
     *
     * @since 1.0
     */
    public static final ClientQueryBlockEntityTagPacketReader
            INSTANCE = new ClientQueryBlockEntityTagPacketReader();

    private ClientQueryBlockEntityTagPacketReader() {}

    @Override
    public @NonNull ClientQueryBlockEntityTagPacket read(@NonNull ByteBuf buf,
                                                         @NonNull JetRegistryManager registryManager) {
        return new ClientQueryBlockEntityTagPacket(
                VarIntNetworkCodec.INSTANCE.read(buf, registryManager),
                BlockPositionNetworkCodec.INSTANCE.read(buf, registryManager)
        );
    }
}