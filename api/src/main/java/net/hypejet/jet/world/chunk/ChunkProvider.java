package net.hypejet.jet.world.chunk;

import net.hypejet.jet.world.World;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a functional interface that creates chunks of {@linkplain World a world}.
 *
 * @since 1.0
 */
@FunctionalInterface
public interface ChunkProvider {
    /**
     * Creates a chunk.
     *
     * @param builder a builder of the chunk
     * @param chunkX an {@code X} value of coordinate of the chunk
     * @param chunkZ an {@code Z} value of coordinate of the chunk
     * @param world a world that the chunk is created for
     * @since 1.0
     */
    void provide(@NonNull ChunkBuilder builder, int chunkX, int chunkZ, @NonNull World world);
}