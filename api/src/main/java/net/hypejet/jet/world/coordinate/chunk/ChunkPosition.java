package net.hypejet.jet.world.coordinate.chunk;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.Contract;

/**
 * Represents position of {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk}.
 *
 * @param chunkX an {@code X} axis value that the position should have
 * @param chunkZ an {@code Z} axis value that the position should have
 * @since 1.0
 * @see net.hypejet.jet.world.chunk.Chunk
 */
public record ChunkPosition(int chunkX, int chunkZ) {
    /**
     * Gets a squared distance between this {@linkplain ChunkPosition chunk position}
     * and {@linkplain ChunkPosition a chunk position} specified.
     *
     * @param position the second chunk position
     * @return the distance
     * @since 1.0
     */
    @Contract(pure = true)
    public @IntRange(from = 0) int distanceSquared(@NonNull ChunkPosition position) {
        NullabilityUtil.requireNonNull(position, "position");
        int xDistance = distance(this.chunkX, position.chunkX);
        int zDistance = distance(this.chunkZ, position.chunkZ);
        return xDistance * xDistance + zDistance * zDistance;
    }

    private static @IntRange(from = 0) int distance(int first, int second) {
        int max = Math.max(first, second);
        int min = Math.min(first, second);
        return Math.abs(max - min);
    }
}