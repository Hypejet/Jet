package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which initializes {@linkplain JetChunk a chunk} for a client.
 *
 * @param chunkPosition a position of the chunk
 * @param chunk the chunk to initialize
 * @since 1.0
 * @see JetChunk
 * @see ServerPacket
 */
public record ServerChunkAndLightDataPlayPacket(@NotNull ChunkPosition chunkPosition, @NotNull JetChunk chunk)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerChunkAndLightDataPlayPacket server chunk and light data play packet}.
     *
     * @param chunkPosition a position of the chunk
     * @param chunk the chunk to initialize
     * @since 1.0
     */
    public ServerChunkAndLightDataPlayPacket {
        NullabilityUtil.requireNonNull(chunkPosition, "chunk position");
        NullabilityUtil.requireNonNull(chunk, "chunk");
    }
}