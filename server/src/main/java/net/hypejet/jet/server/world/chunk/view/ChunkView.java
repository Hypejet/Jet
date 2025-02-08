package net.hypejet.jet.server.world.chunk.view;

import net.hypejet.jet.server.world.coordinate.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a range of {@linkplain ChunkPosition chunk positions}.
 *
 * @param centerChunk a center chunk position that should be in the range
 * @param viewDistance a half of edge length of a square that should represent the range
 * @since 1.0
 */
public record ChunkView(@NonNull ChunkPosition centerChunk, byte viewDistance) {
    /**
     * Gets a minimum {@code X} value of {@linkplain ChunkPosition a chunk position}, which can be in this view.
     *
     * @return the value
     * @since 1.0
     */
    public int minimumChunkX() {
        return this.centerChunk.chunkX() - this.viewDistance - 1;
    }

    /**
     * Gets a minimum {@code Z} value of {@linkplain ChunkPosition a chunk position}, which can be in this view.
     *
     * @return the value
     * @since 1.0
     */
    public int minimumChunkZ() {
        return this.centerChunk.chunkZ() - this.viewDistance - 1;
    }

    /**
     * Gets a maximum {@code X} value of {@linkplain ChunkPosition a chunk position}, which can be in this view.
     *
     * @return the value
     * @since 1.0
     */
    public int maximumChunkX() {
        return this.centerChunk.chunkX() + this.viewDistance + 1;
    }

    /**
     * Gets a maximum {@code Z} value of {@linkplain ChunkPosition a chunk position}, which can be in this view.
     *
     * @return the value
     * @since 1.0
     */
    public int maximumChunkZ() {
        return this.centerChunk.chunkZ() + this.viewDistance + 1;
    }

    /**
     * Gets whether {@linkplain ChunkPosition a chunk position} specified is within this chunk view.
     *
     * @param position the chunk position
     * @return {@code true} fi the chunk position specified is within this chunk view, {@code false} otherwise
     * @since 1.0
     */
    public boolean isInView(@NonNull ChunkPosition position) {
        int chunkX = position.chunkX();
        int chunkZ = position.chunkZ();
        return chunkX <= this.maximumChunkX() && chunkX >= this.minimumChunkX()
                && chunkZ <= this.maximumChunkZ() && chunkZ >= this.minimumChunkZ();
    }
}