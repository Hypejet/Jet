package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.ChunkNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.server.world.coordinate.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerChunkAndLightDataPlayPacket a server chunk and light data play packet}.
 *
 * @since 1.0
 * @see ServerChunkAndLightDataPlayPacket
 * @see NetworkWriter
 */
public final class ServerChunkAndLightDataPlayPacketWriter
        implements NetworkWriter<ServerChunkAndLightDataPlayPacket> {
    /**
     * An instance of the {@linkplain ServerChunkAndLightDataPlayPacketWriter server chunk and light data play packet
     * writer}.
     *
     * @since 1.0
     */
    public static final ServerChunkAndLightDataPlayPacketWriter
            INSTANCE = new ServerChunkAndLightDataPlayPacketWriter();

    private ServerChunkAndLightDataPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerChunkAndLightDataPlayPacket object) {
        ChunkPosition position = object.chunkPosition();
        buf.writeInt(position.chunkX());
        buf.writeInt(position.chunkZ());
        ChunkNetworkWriter.INSTANCE.write(buf, object.chunk());
    }
}