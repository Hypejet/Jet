package net.hypejet.jet.server.util.coordinate;

import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.coordinate.biome.BiomePosition;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a utility for converting positions from chunk-relative to absolute.
 *
 * @since 1.0
 */
public final class AbsolutePositionUtil {

    private AbsolutePositionUtil() {}

    /**
     * Creates an absolute {@linkplain BlockPosition block position}
     * from {@linkplain ChunkRelativeBlockPosition a chunk-relative block position}, which is relative
     * to {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} with {@linkplain ChunkPosition a chunk position}
     * specified.
     *
     * @param position the chunk-relative block position
     * @param chunkPosition the chunk position
     * @return the block position
     * @since 1.0
     */
    public static @NonNull BlockPosition from(@NonNull ChunkRelativeBlockPosition position,
                                              @NonNull ChunkPosition chunkPosition) {
        return new BlockPosition(
                createAbsoluteValue(position.relativeX(), chunkPosition.chunkX(), ChunkPaletteType.BLOCK_STATE),
                position.absoluteY(),
                createAbsoluteValue(position.relativeZ(), chunkPosition.chunkZ(), ChunkPaletteType.BLOCK_STATE)
        );
    }

    /**
     * Creates an absolute {@linkplain BiomePosition biome position}
     * from {@linkplain ChunkRelativeBiomePosition a chunk-relative biome position}, which is relative
     * to {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} with {@linkplain ChunkPosition a chunk position}
     * specified.
     *
     * @param position the chunk-relative biome position
     * @param chunkPosition the chunk position
     * @return the biome position
     * @since 1.0
     */
    public static @NonNull BiomePosition from(@NonNull ChunkRelativeBiomePosition position,
                                              @NonNull ChunkPosition chunkPosition) {
        return new BiomePosition(
                createAbsoluteValue(position.relativeX(), chunkPosition.chunkX(), ChunkPaletteType.BIOME),
                position.absoluteY(),
                createAbsoluteValue(position.relativeZ(), chunkPosition.chunkZ(), ChunkPaletteType.BLOCK_STATE)
        );
    }

    private static int createAbsoluteValue(int relativeValue, int chunkPositionValue,
                                           @NonNull ChunkPaletteType paletteType) {
        return chunkPositionValue * paletteType.axisLength() + relativeValue;
    }
}