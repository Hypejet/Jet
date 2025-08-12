package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain ServerPacket a server packet}, which requests a client to invalidate data
 * of {@linkplain JetChunk a chunk} with {@linkplain ChunkPosition a chunk position}
 * specified that was sent by a server.
 *
 * @param chunkPosition the chunk position
 * @since 1.0
 * @see JetChunk
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
        Objects.requireNonNull(chunkPosition, "chunk position");
    }
}