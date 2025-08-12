package net.hypejet.jet.server.util.coordinate;

import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.section.ChunkSectionPosition;
import net.hypejet.jet.world.coordinate.Coordinate;
import net.hypejet.jet.world.coordinate.biome.BiomePosition;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a utility for {@linkplain ChunkPosition chunk position} management,
 *
 * @since 1.0
 * @see ChunkPosition
 */
public final class ChunkPositionUtil {

    private ChunkPositionUtil() {}

    /**
     * Creates a {@linkplain ChunkPosition chunk position} of a {@linkplain JetChunk chunk}
     * that the specified {@linkplain Coordinate coordinate} belongs to.
     *
     * @param coordinate the coordinate
     * @return the chunk position
     * @since 1.0
     */
    public static @NonNull ChunkPosition fromCoordinate(@NonNull Coordinate<?> coordinate) {

        return new ChunkPosition(
                toChunkPositionValue(coordinate.blockX(), ChunkPaletteType.BLOCK_STATE),
                toChunkPositionValue(coordinate.blockZ(), ChunkPaletteType.BLOCK_STATE)
        );
    }

    /**
     * Creates {@linkplain ChunkPosition a chunk position} of {@linkplain JetChunk a chunk}
     * that {@linkplain BiomePosition a biome position} specified belongs to.
     *
     * @param position the biome position
     * @return the chunk position
     * @since 1.0
     */
    public static @NonNull ChunkPosition fromBiomePosition(@NonNull BiomePosition position) {
        return new ChunkPosition(
                toChunkPositionValue(position.x(), ChunkPaletteType.BIOME),
                toChunkPositionValue(position.z(), ChunkPaletteType.BIOME)
        );
    }

    private static int toChunkPositionValue(int value, @NonNull ChunkPaletteType paletteType) {
        // A chunk is a column of chunk sections
        return ChunkSectionPosition.createChunkSectionCoordinateValue(value, paletteType);
    }
}