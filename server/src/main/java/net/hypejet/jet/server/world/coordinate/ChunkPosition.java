package net.hypejet.jet.server.world.coordinate;

import net.hypejet.jet.data.model.api.coordinate.Coordinate;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.coordinate.BiomePosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.Contract;

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

    /**
     * Creates {@linkplain ChunkPosition a chunk position} of
     * {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk} that {@linkplain Coordinate a coordinate}
     * specified belongs to.
     *
     * @param coordinate the coordinate
     * @return the chunk position
     * @since 1.0
     */
    public static @NonNull ChunkPosition fromCoordinate(@NonNull Coordinate<?> coordinate) {
        return new ChunkPosition(
                toChunkCoordinate(coordinate.blockX(), CHUNK_AXIS_BLOCK_LENGTH),
                toChunkCoordinate(coordinate.blockZ(), CHUNK_AXIS_BLOCK_LENGTH)
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

    private static @IntRange(from = 0) int distance(int first, int second) {
        int max = Math.max(first, second);
        int min = Math.min(first, second);
        return Math.abs(max - min);
    }
}