package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which initializes {@linkplain Chunk a chunk} for a client.
 *
 * @param chunkX an {@code X} value of coordinate of the chunk
 * @param chunkZ an {@code Z} value of coordinate of the chunk
 * @param chunk the chunk to initialize
 * @since 1.0
 * @see Chunk
 * @see ServerPacket
 */
public record ServerChunkAndLightDataPlayPacket(int chunkX, int chunkZ, @NotNull Chunk chunk) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerChunkAndLightDataPlayPacket server chunk and light data play packet}.
     *
     * @param chunkX an {@code X} value of coordinate of the chunk
     * @param chunkZ an {@code Z} value of coordinate of the chunk
     * @param chunk the chunk to initialize
     * @since 1.0
     */
    public ServerChunkAndLightDataPlayPacket {
        NullabilityUtil.requireNonNull(chunk, "chunk");
    }
}