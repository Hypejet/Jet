package net.hypejet.jet.world.chunk;

/**
 * Represents a position of a {@linkplain Chunk chunk}.
 *
 * @param chunkX a position of the chunk at an {@code X} axis
 * @param chunkZ a position of the chunk at a {@code Z} axis
 * @since 1.0
 * @author Codestech
 * @see Chunk
 */
public record ChunkPosition(int chunkX, int chunkZ) {}