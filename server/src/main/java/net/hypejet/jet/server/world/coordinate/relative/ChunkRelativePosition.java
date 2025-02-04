package net.hypejet.jet.server.world.coordinate.relative;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a coordinate relative to {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk}.
 *
 * <p>Horizontal values of the chunk-relative position are relative to each chunk palettes of the chunk, which
 * have the same {@linkplain ChunkPaletteType chunk palette type} as the position.</p>
 *
 * @param paletteRelativeX an {@code X} value of the coordinate, relative to all chunk palettes of type specified
 *                         of the chunk
 * @param absoluteY an absolute {@code Y} value of the coordinate
 * @param paletteRelativeZ an {@code Z} value of the coordinate, relative to all chunk palettes of type specified
 *                         of the chunk
 * @param paletteType a type of chunk palettes that horizontal values are relative to
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 */
public record ChunkRelativePosition(byte paletteRelativeX, int absoluteY, byte paletteRelativeZ,
                                    @NonNull ChunkPaletteType paletteType) {
    /**
     * Constructs the {@linkplain ChunkRelativePosition chunk-relative position}.
     *
     * @param paletteRelativeX an {@code X} value of the coordinate, relative to all chunk palettes of type specified
     *                         of the chunk
     * @param absoluteY an absolute {@code Y} value of the coordinate
     * @param paletteRelativeZ an {@code Z} value of the coordinate, relative to all chunk palettes of type specified
     *                         of the chunk
     * @param paletteType a type of chunk palettes that horizontal values are relative to
     * @since 1.0
     */
    public ChunkRelativePosition {
        NullabilityUtil.requireNonNull(paletteType, "palette type");
        paletteType.validateCoordinateValue(paletteRelativeX);
        paletteType.validateCoordinateValue(paletteRelativeZ);
    }

    /**
     * Creates {@linkplain ChunkRelativePosition a chunk-relative position}
     * for {@linkplain BlockPosition an absolute block position} specified.
     *
     * @param position the absolute block position
     * @return the chunk-relative position
     * @since 1.0
     */
    public static @NonNull ChunkRelativePosition from(@NonNull BlockPosition position) {
        return from(position.blockX(), position.blockY(), position.blockZ(), ChunkPaletteType.BLOCK_STATE);
    }

    /**
     * Creates {@linkplain ChunkRelativePosition a chunk-relative position} for absolute coordinate values specified.
     * Horizontal values of the chunk-relative position are relative to each chunk palettes of the chunk
     * of {@linkplain ChunkPaletteType a chunk palette type} specified.
     *
     * @param absoluteX an absolute {@code X} value of the coordinate
     * @param absoluteY an absolute {@code Y} value of the coordinate
     * @param absoluteZ an absolute {@code Z} value of the coordinate
     * @param paletteType the chunk palette type
     * @return the chunk-relative position
     * @since 1.0
     */
    public static @NonNull ChunkRelativePosition from(int absoluteX, int absoluteY, int absoluteZ,
                                                      @NonNull ChunkPaletteType paletteType) {
        return new ChunkRelativePosition(
                createPaletteRelativeValue(absoluteX, paletteType),
                absoluteY,
                createPaletteRelativeValue(absoluteZ, paletteType),
                paletteType
        );
    }

    /**
     * Creates {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position}
     * for {@linkplain net.hypejet.jet.server.world.chunk.palette.ChunkPalette a chunk palette}
     * that this chunk-relative position belongs to.
     *
     * @return the chunk-palette-relative position
     * @since 1.0
     */
    public @NonNull ChunkPaletteRelativePosition toChunkPaletteRelative() {
        return new ChunkPaletteRelativePosition(
                this.paletteRelativeX,
                createPaletteRelativeValue(this.absoluteY, paletteType),
                this.paletteRelativeZ,
                this.paletteType
        );
    }

    private static byte createPaletteRelativeValue(int value, @NonNull ChunkPaletteType paletteType) {
        byte axisLength = paletteType.axisLength();
        byte result = (byte) (value % axisLength);
        return result < 0 ? (byte) (axisLength + result) : result;
    }
}