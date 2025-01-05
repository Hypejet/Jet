package net.hypejet.jet.server.world.chunk.palette.update;

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
public record ChunkPaletteUpdate(byte sectionX, byte sectionY, byte sectionZ, int newElement) {}