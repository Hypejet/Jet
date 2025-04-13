package net.hypejet.jet.server.util.coordinate;

import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.BiomePosition;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a utility for creating positions relative to {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk}
 * from absolute positions.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.chunk.Chunk
 */
public final class ChunkRelativePositionUtil {
    
    private ChunkRelativePositionUtil() {}

    /**
     * Creates {@linkplain ChunkRelativeBlockPosition a chunk-relative block position}
     * for {@linkplain BlockPosition a block position} specified.
     *
     * @param position the block position
     * @return the chunk-relative block position
     * @since 1.0
     */
    public static @NonNull ChunkRelativeBlockPosition from(@NonNull BlockPosition position) {
        ChunkPaletteType paletteType = ChunkPaletteType.BLOCK_STATE;
        return new ChunkRelativeBlockPosition(
                ChunkPaletteRelativePosition.createPaletteRelativeValue(position.blockX(), paletteType),
                position.blockY(),
                ChunkPaletteRelativePosition.createPaletteRelativeValue(position.blockZ(), paletteType)
        );
    }

    /**
     * Creates {@linkplain ChunkRelativeBiomePosition a chunk-relative biome position}
     * for {@linkplain BiomePosition a biome position} specified.
     *
     * @param position the biome position
     * @return the chunk-relative biome position
     * @since 1.0
     */
    public static @NonNull ChunkRelativeBiomePosition from(@NonNull BiomePosition position) {
        ChunkPaletteType paletteType = ChunkPaletteType.BIOME;
        return new ChunkRelativeBiomePosition(
                ChunkPaletteRelativePosition.createPaletteRelativeValue(position.x(), paletteType),
                (short) position.y(),
                ChunkPaletteRelativePosition.createPaletteRelativeValue(position.z(), paletteType)
        );
    }
}