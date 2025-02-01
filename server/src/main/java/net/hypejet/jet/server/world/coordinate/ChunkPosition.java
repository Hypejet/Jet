package net.hypejet.jet.server.world.coordinate;

import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents position of {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk}.
 *
 * @param chunkX an {@code X} axis value that the position should have
 * @param chunkZ an {@code Z} axis value that the position should have
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 */
public record ChunkPosition(int chunkX, int chunkZ) {
    /**
     * Creates {@linkplain ChunkPosition a chunk position} of
     * {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk} that {@linkplain BlockPosition a block position}
     * specified belongs to.
     *
     * @param position the block position
     * @return the chunk position
     * @since 1.0
     */
    public static @NonNull ChunkPosition fromBlockPosition(@NonNull BlockPosition position) {
        return new ChunkPosition(blockToChunkCoordinate(position.blockX()), blockToChunkCoordinate(position.blockZ()));
    }

    private static int blockToChunkCoordinate(int blockCoordinate) {
        return Math.floorDiv(blockCoordinate, ChunkPaletteType.BLOCK_STATE.axisLength());
    }
}