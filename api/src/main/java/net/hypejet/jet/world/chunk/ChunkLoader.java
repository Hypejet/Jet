package net.hypejet.jet.world.chunk;

import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that manages loading and saving of {@linkplain Chunk chunks}.
 *
 * @since 1.0
 * @see Chunk
 */
public interface ChunkLoader {
    /**
     * Creates {@linkplain Chunk a chunk} that should be present at {@linkplain ChunkPosition a chunk position}
     * specified in {@linkplain World a world specified}.
     *
     * @param position a position
     * @param world the world
     * @return the chunk
     * @since 1.0
     */
    @NonNull Chunk load(@NonNull ChunkPosition position, @NonNull World world);

    /**
     * Saves data of {@linkplain Chunk a chunk} specified, which is at {@linkplain ChunkPosition a chunk position}
     * specified in {@linkplain World a world} specified.
     *
     * @param position the chunk position
     * @param world the world
     * @param chunk the chunk
     * @since 1.0
     */
    void save(@NonNull ChunkPosition position, @NonNull World world, @NonNull Chunk chunk);
}