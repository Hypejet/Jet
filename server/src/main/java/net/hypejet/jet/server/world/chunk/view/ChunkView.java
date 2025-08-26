package net.hypejet.jet.server.world.chunk.view;

import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * An area of {@linkplain JetChunk chunks} visible to a {@linkplain JetPlayer player}.
 *
 * @param centerChunk a chunk position of the center chunk of the area
 * @param viewDistance a number of chunks in each direction from the center that are visible to the player
 * @since 1.0
 * @see JetChunk
 * @see JetPlayer
 */
@NullMarked
public record ChunkView(ChunkPosition centerChunk, byte viewDistance) {
    /**
     * Constructs the {@linkplain ChunkView chunk view}.
     *
     * @param centerChunk a chunk position of the chunk that should be center of the area
     * @param viewDistance a number of chunks in each direction from the center that should be visible to the player
     * @since 1.0
     */
    public ChunkView {
        Objects.requireNonNull(centerChunk, "center chunk");
    }

    /**
     * Consumes {@linkplain ChunkPosition chunk positions} of {@linkplain JetChunk chunks}
     * that need to be sent to the client to make it have the same {@linkplain ChunkView chunk view} as this.
     *
     * @param consumer the consumer to consume the chunk positions with
     * @since 1.0
     */
    public void forEach(Consumer<ChunkPosition> consumer) {
        for (int chunkX = this.minimumChunkX(); chunkX <= this.maximumChunkX(); chunkX++) {
            for (int chunkZ = this.minimumChunkZ(); chunkZ <= this.maximumChunkZ(); chunkZ++) {
                consumer.accept(new ChunkPosition(chunkX, chunkZ));
            }
        }
    }

    /**
     * Gets whether the specified {@linkplain ChunkPosition chunk position}
     * is within this {@linkplain ChunkView chunk view}.
     *
     * @param position the chunk position to check whether it is within this chunk view
     * @return {@code true} if the specified chunk position is within this chunk view, {@code false} otherwise
     * @since 1.0
     */
    public boolean isInView(ChunkPosition position) {
        for (int chunkX = this.minimumChunkX(); chunkX <= this.maximumChunkX(); chunkX++)
            for (int chunkZ = this.minimumChunkZ(); chunkZ <= this.maximumChunkZ(); chunkZ++)
                if (position.chunkX() == chunkX && position.chunkZ() == chunkZ) return true;
        return false;
    }

    private int minimumChunkX() {
        return this.centerChunk.chunkX() - this.viewDistance - 1;
    }

    private int minimumChunkZ() {
        return this.centerChunk.chunkZ() - this.viewDistance - 1;
    }

    private int maximumChunkX() {
        return this.centerChunk.chunkX() + this.viewDistance + 1;
    }

    private int maximumChunkZ() {
        return this.centerChunk.chunkZ() + this.viewDistance + 1;
    }
}