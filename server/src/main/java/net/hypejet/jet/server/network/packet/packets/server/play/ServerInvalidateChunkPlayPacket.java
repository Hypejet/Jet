package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.coordinate.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which requests a client to invalidate data
 * of {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk} with {@linkplain ChunkPosition a chunk position}
 * specified that was sent by a server.
 *
 * @param chunkPosition the chunk position
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 * @see ChunkPosition
 */
public record ServerInvalidateChunkPlayPacket(@NonNull ChunkPosition chunkPosition) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerInvalidateChunkPlayPacket server invalidate chunk play packet}.
     *
     * @param chunkPosition the chunk position
     * @since 1.0
     */
    public ServerInvalidateChunkPlayPacket {
        NullabilityUtil.requireNonNull(chunkPosition, "chunk position");
    }
}