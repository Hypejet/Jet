package net.hypejet.jet.server.network.codec.game.world.coordinate.chunk;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ChunkPosition a chunk position}.
 *
 * @since 1.0
 * @see ChunkPosition
 * @see NetworkWriter
 */
public final class ChunkPositionNetworkWriter implements NetworkWriter<ChunkPosition> {
    /**
     * An instance of the {@linkplain ChunkPositionNetworkWriter chunk position network writer}.
     *
     * @since 1.0
     */
    public static final ChunkPositionNetworkWriter INSTANCE = new ChunkPositionNetworkWriter();

    private ChunkPositionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ChunkPosition object) {
        buf.writeInt(object.chunkZ());
        buf.writeInt(object.chunkX());
    }
}