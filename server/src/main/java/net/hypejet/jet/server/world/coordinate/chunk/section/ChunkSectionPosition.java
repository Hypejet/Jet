package net.hypejet.jet.server.world.coordinate.chunk.section;

import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an absolute position
 * of {@linkplain net.hypejet.jet.world.chunk.section.ChunkSection a chunk section}.
 *
 * @param sectionX a chunk-section {@code X} value of the chunk section
 * @param sectionY a chunk-section {@code Y} value of the chunk section
 * @param sectionZ a chunk-section {@code Z} value of the chunk section
 * @see net.hypejet.jet.world.chunk.section.ChunkSection
 * @since 1.0
 */
public record ChunkSectionPosition(int sectionX, int sectionY, int sectionZ) {
    /**
     * Creates {@linkplain ChunkSectionPosition a chunk section position} for a height specified
     * in {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} at {@linkplain ChunkPosition a chunk position}
     * specified.
     *
     * @param chunkPosition the chunk position
     * @param height the height
     * @param paletteType a chunk palette type, whose axis length should be used for chunk-section position
     *                    value creation
     * @return the chunk section position
     * @since 1.0
     */
    public static @NonNull ChunkSectionPosition from(@NonNull ChunkPosition chunkPosition, int height,
                                                     @NonNull ChunkPaletteType paletteType) {
        return new ChunkSectionPosition(
                chunkPosition.chunkX(),
                createChunkSectionCoordinateValue(height, paletteType),
                chunkPosition.chunkZ()
        );
    }

    /**
     * Creates a chunk-section position value for an absolute value specified.
     *
     * @param value the absolute value
     * @param paletteType a chunk palette type, whose axis length should be used for chunk-section position
     *                    value creation
     * @return the chunk-section position value
     * @since 1.0
     */
    public static int createChunkSectionCoordinateValue(int value, @NonNull ChunkPaletteType paletteType) {
        return Math.floorDiv(value, paletteType.axisLength());
    }
}