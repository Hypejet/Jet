package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBlockStatePlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerUpdateBlockStatePlayPacket a server update block state play packet}.
 *
 * @since 1.0
 * @see NetworkWriter
 * @see ServerUpdateBlockStatePlayPacket
 */
public final class ServerUpdateBlockStatePlayPacketWriter implements NetworkWriter<ServerUpdateBlockStatePlayPacket> {
    /**
     * An instance
     * of the {@linkplain ServerUpdateBlockStatePlayPacketWriter server update block state play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerUpdateBlockStatePlayPacketWriter INSTANCE = new ServerUpdateBlockStatePlayPacketWriter();

    private ServerUpdateBlockStatePlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerUpdateBlockStatePlayPacket object) {
        BlockPositionNetworkCodec.INSTANCE.write(buf, registryManager, object.position());
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.blockStateIdentifier());
    }
}