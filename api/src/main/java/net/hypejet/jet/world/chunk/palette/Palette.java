package net.hypejet.jet.world.chunk.palette;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a container of data of a {@linkplain net.hypejet.jet.world.chunk.ChunkSection chunk section}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.world.chunk.ChunkSection
 */
public sealed interface Palette permits DirectPalette, IndirectPalette, SingleValuedPalette {
    /**
     * Gets entries of the data of the palette.
     *
     * @return the entries
     * @since 1.0
     */
    long @NonNull [] entries();
}