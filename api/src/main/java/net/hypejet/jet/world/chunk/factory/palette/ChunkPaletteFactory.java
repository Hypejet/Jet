package net.hypejet.jet.world.chunk.factory.palette;

import net.hypejet.jet.world.chunk.section.ChunkPalette;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents a factory of {@linkplain ChunkPalette chunk palettes}.
 *
 * @param <E> a type of elements of chunk palettes that the factory creates
 * @since 1.0
 * @see ChunkPalette
 */
public interface ChunkPaletteFactory<E> {
    /**
     * Creates {@linkplain ChunkPalette a chunk palette}, whose element list is filled with an element specified.
     *
     * @param element the element
     * @return the chunk palette
     * @since 1.0
     */
    @NonNull ChunkPalette<E> createSingleValued(@NonNull E element);

    /**
     * Creates {@linkplain ChunkPalette a chunk palette} with an element list specified.
     *
     * <p>The element list should be ordered in a way specified in javadoc header
     * of {@linkplain ChunkPalette a chunk palette}.</p>
     *
     * @param elements the element list
     * @return the chunk palette
     * @since 1.0
     */
    @NonNull ChunkPalette<E> createDirect(@NonNull List<E> elements);
}