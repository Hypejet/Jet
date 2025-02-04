package net.hypejet.jet.server.world.coordinate;

import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.coordinate.BiomePosition;
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
     * A length of each axis of {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk} in blocks.
     *
     * @since 1.0
     */
    // Chunk is a column of chunk sections, which contain chunk palettes
    public static final byte CHUNK_AXIS_BLOCK_LENGTH = ChunkPaletteType.BLOCK_STATE.axisLength();

    /**
     * A length of each axis of {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk} in biomes.
     *
     * @since 1.0
     */
    // Chunk is a column of chunk sections, which contain chunk palettes
    public static final byte
            CHUNK_AXIS_BIOME_LENGTH = (byte) (CHUNK_AXIS_BLOCK_LENGTH / ChunkPaletteType.BIOME.axisLength());

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
        return new ChunkPosition(
                toChunkCoordinate(position.blockX(), CHUNK_AXIS_BLOCK_LENGTH),
                toChunkCoordinate(position.blockZ(), CHUNK_AXIS_BIOME_LENGTH)
        );
    }

    /**
     * Creates {@linkplain ChunkPosition a chunk position} of
     * {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk} that {@linkplain BiomePosition a biome position}
     * specified belongs to.
     *
     * @param position the biome position
     * @return the chunk position
     * @since 1.0
     */
    public static @NonNull ChunkPosition fromBiomePosition(@NonNull BiomePosition position) {
        return new ChunkPosition(
                toChunkCoordinate(position.x(), CHUNK_AXIS_BIOME_LENGTH),
                toChunkCoordinate(position.z(), CHUNK_AXIS_BIOME_LENGTH)
        );
    }

    private static int toChunkCoordinate(int blockCoordinate, byte axisLength) {
        return Math.floorDiv(blockCoordinate, axisLength);
    }
}