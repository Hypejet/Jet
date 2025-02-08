package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerCenterChunkPlayPacket;
import net.hypejet.jet.server.world.coordinate.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerCenterChunkPlayPacket a center chunk play packet}.
 *
 * @since 1.0
 * @see ServerCenterChunkPlayPacket
 * @see NetworkWriter
 */
public final class ServerCenterChunkPlayPacketWriter implements NetworkWriter<ServerCenterChunkPlayPacket> {

    /**
     * An instance of the {@linkplain ServerCenterChunkPlayPacketWriter server center chunk play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerCenterChunkPlayPacketWriter INSTANCE = new ServerCenterChunkPlayPacketWriter();

    private ServerCenterChunkPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCenterChunkPlayPacket object) {
        // Chunk position in this packet is encoded differently
        ChunkPosition chunkPosition = object.chunkPosition();
        VarIntNetworkCodec.INSTANCE.write(buf, chunkPosition.chunkX());
        VarIntNetworkCodec.INSTANCE.write(buf, chunkPosition.chunkZ());
    }
}