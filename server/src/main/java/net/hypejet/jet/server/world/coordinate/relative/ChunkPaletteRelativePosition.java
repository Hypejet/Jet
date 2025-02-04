package net.hypejet.jet.server.world.coordinate.relative;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a coordinate relative
 * to {@linkplain net.hypejet.jet.server.world.chunk.palette.ChunkPalette a chunk palette}.
 *
 * @param x a chunk-palette-relative {@code X} value of the coordinate
 * @param y a chunk-palette-relative {@code Y} value of the coordinate
 * @param z a chunk-palette-relative {@code Z} value of the coordinate
 * @param paletteType a type of chunk palette that the coordinate is relative to
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.section.ChunkSection
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
        NullabilityUtil.requireNonNull(paletteType, "palette type");
        paletteType.validateCoordinateValue(x);
        paletteType.validateCoordinateValue(y);
        paletteType.validateCoordinateValue(z);
    }
}