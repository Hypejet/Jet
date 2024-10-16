package net.hypejet.jet.world;

import net.hypejet.jet.data.model.api.coordinate.Coordinate;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a container of Minecraft {@linkplain ??? blocks}
 * and {@linkplain net.hypejet.jet.entity.Entity entities}.
 *
 * @since 1.0
 * @author Codestech
 * @see ???
 * @see net.hypejet.jet.entity.Entity
 */
public interface World {
    /**
     * Gets a {@linkplain UUID unique identifier} of the world.
     *
     * @return the unique identifier
     * @since 1.0
     */
    @NonNull UUID uniqueId();

    /**
     * Gets entities, which are in this world.
     *
     * @return the entities
     */
    @NonNull Collection<Entity> entities();

    /**
     * Gets a chunk at a position specified.
     *
     * @param coordinate the position
     * @return the chunk, {@code null} if there is no a chunk at the position specified
     * @since 1.0
     */
    @Nullable Chunk chunkAt(@NonNull Coordinate<?> coordinate);

    /**
     * Gets a chunk at a position specified.
     *
     * @param chunkX a position of the chunk at an {@code X} axis
     * @param chunkZ a position of the chunk at a {@code Z} axis
     * @return the chunk, {@code null} if there is no a chunk at the position specified
     * @since 1.0
     */
    @Nullable Chunk chunkAt(int chunkX, int chunkZ);


    /**
     * Gets a chunk at a position specified.
     *
     * @param position the position
     * @return the chunk, {@code null} if there is no a chunk at the position specified
     * @since 1.0
     */
    @Nullable Chunk chunkAt(@NonNull ChunkPosition position);
}