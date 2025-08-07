package net.hypejet.jet.server.world.coordinate.chunk.palette.relative;

import java.util.Objects;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a coordinate relative to {@linkplain AbstractChunkPalette a chunk palette}.
 *
 * @param x a chunk-palette-relative {@code X} value of the coordinate
 * @param y a chunk-palette-relative {@code Y} value of the coordinate
 * @param z a chunk-palette-relative {@code Z} value of the coordinate
 * @param paletteType a type of chunk palette that the coordinate is relative to
 * @since 1.0
 * @see JetChunkSection
 */
public record ChunkPaletteRelativePosition(byte x, byte y, byte z, @NonNull ChunkPaletteType paletteType) {
    /**
     * Constructs the {@linkplain ChunkPaletteRelativePosition chunk-palette-relative position}.
     *
     * @param x a chunk-palette-relative {@code X} value of the coordinate
     * @param y a chunk-palette-relative {@code Y} value of the coordinate
     * @param z a chunk-palette-relative {@code Z} value of the coordinate
     * @param paletteType a type of chunk palette that the coordinate is relative to
     * @since 1.0
     */
    public ChunkPaletteRelativePosition {
        Objects.requireNonNull(paletteType, "palette type");
        paletteType.validateCoordinateValue(x);
        paletteType.validateCoordinateValue(y);
        paletteType.validateCoordinateValue(z);
    }

    /**
     * Constructs the {@linkplain ChunkPaletteRelativePosition chunk-palette-relative position}.
     *
     * @param x a chunk-palette-relative {@code X} value of the coordinate
     * @param absoluteY an absolute {@code Y} value, which should be converted
     *                  to a chunk-palette-relative coordinate value
     * @param z a chunk-palette-relative {@code Z} value of the coordinate
     * @param paletteType a type of chunk palette that the coordinate is relative to
     * @since 1.0
     */
    private ChunkPaletteRelativePosition(byte x, int absoluteY, byte z, @NonNull ChunkPaletteType paletteType) {
        this(x, createPaletteRelativeValue(absoluteY, paletteType), z, paletteType);
    }

    /**
     * Creates {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position}
     * from {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified.
     *
     * @param position the chunk-relative block position
     * @return the chunk-palette-relative position
     * @since 1.0
     */
    public static @NonNull ChunkPaletteRelativePosition from(@NonNull ChunkRelativeBlockPosition position) {
        return new ChunkPaletteRelativePosition(
                position.relativeX(), position.absoluteY(),
                position.relativeZ(), ChunkPaletteType.BLOCK_STATE
        );
    }

    /**
     * Creates {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position}
     * from {@linkplain ChunkRelativeBiomePosition a chunk-relative biome position} specified.
     *
     * @param position the chunk-relative biome position
     * @return the chunk-palette-relative position
     * @since 1.0
     */
    public static @NonNull ChunkPaletteRelativePosition from(@NonNull ChunkRelativeBiomePosition position) {
        return new ChunkPaletteRelativePosition(
                position.relativeX(), position.absoluteY(),
                position.relativeZ(), ChunkPaletteType.BIOME
        );
    }

    /**
     * Creates a coordinate value relative
     * to {@linkplain net.hypejet.jet.world.chunk.section.ChunkPalette a chunk palette}
     * from an absolute coordinate value specified.
     *
     * @param value the absolute value
     * @param paletteType a type of the chunk palette
     * @return the chunk-palette-relative coordinate value
     * @since 1.0
     */
    public static byte createPaletteRelativeValue(int value, @NonNull ChunkPaletteType paletteType) {
        byte axisLength = paletteType.axisLength();
        byte result = (byte) (value % axisLength);
        return result < 0 ? (byte) (axisLength + result) : result;
    }
}