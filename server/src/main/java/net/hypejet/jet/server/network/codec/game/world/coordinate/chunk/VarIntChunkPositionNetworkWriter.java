package net.hypejet.jet.server.network.codec.game.world.coordinate.chunk;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ChunkPosition a chunk position}
 * using variable-length integers.
 *
 * @since 1.0
 * @see ChunkPosition
 * @see NetworkWriter
 */
public final class VarIntChunkPositionNetworkWriter implements NetworkWriter<ChunkPosition> {
    /**
     * An instance of the {@linkplain VarIntChunkPositionNetworkWriter chunk position network writer}.
     *
     * @since 1.0
     */
    public static final VarIntChunkPositionNetworkWriter INSTANCE = new VarIntChunkPositionNetworkWriter();

    private VarIntChunkPositionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ChunkPosition object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.chunkX());
        VarIntNetworkCodec.INSTANCE.write(buf, object.chunkZ());
    }
}