package net.hypejet.jet.server.world.chunk.palette.update;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.coordinate.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a change that should be done in
 * {@linkplain net.hypejet.jet.server.world.chunk.palette.ChunkPalette a chunk palette}.
 *
 * @param position a position where the change should be made
 * @param newElement a new element that element at the position specified should be replaced with
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.palette.ChunkPalette
 */
public record ChunkPaletteUpdate<E>(@NonNull ChunkPaletteRelativePosition position, @NonNull E newElement) {
    /**
     * Constructs the {@linkplain ChunkPaletteUpdate chunk palette update}.
     *
     * @param position a position where the change should be made
     * @param newElement a new element that element at the position specified should be replaced with
     * @since 1.0
     */
    public ChunkPaletteUpdate {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(newElement, "new element");
    }
}