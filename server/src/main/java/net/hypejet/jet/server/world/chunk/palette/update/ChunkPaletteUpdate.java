package net.hypejet.jet.server.world.chunk.palette.update;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a change that should be done in
 * {@linkplain net.hypejet.jet.server.world.chunk.palette.ChunkPalette a chunk palette}.
 *
 * @param sectionX a section-relative {@code X} coordinate of position where the change should be made
 * @param sectionY a section-relative {@code Y} coordinate of position where the change should be made
 * @param sectionZ a section-relative {@code Z} coordinate of position where the change should be made
 * @param newElement a new element that element at the position specified should be replaced with
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.palette.ChunkPalette
 */
public record ChunkPaletteUpdate<E>(byte sectionX, byte sectionY, byte sectionZ, @NonNull E newElement) {
    /**
     * Constructs the {@linkplain ChunkPaletteUpdate chunk palette update}.
     *
     * @param sectionX a section-relative {@code X} coordinate of position where the change should be made
     * @param sectionY a section-relative {@code Y} coordinate of position where the change should be made
     * @param sectionZ a section-relative {@code Z} coordinate of position where the change should be made
     * @param newElement a new element that element at the position specified should be replaced with
     * @since 1.0
     */
    public ChunkPaletteUpdate {
        NullabilityUtil.requireNonNull(newElement, "new element");
    }
}