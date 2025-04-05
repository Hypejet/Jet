package net.hypejet.jet.world.chunk.section;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Set;

/**
 * Represents a storage of data of {@linkplain ChunkSection a chunk section}.
 *
 * <p>Elements of the data can be represented in a list, where one list element corresponds one data element.
 * The elements are stored in a way, where the element index modulo chunk axis length is a palette-relative {@code X},
 * element index divided by chunk axis length is a palette-relative {@code Z} and element index divided by squared
 * chunk axis length is a palette-relative {@code Y}. The chunk axis length depends on type of elements that
 * the palette stores.</p>
 *
 * @param <E> a type of elements that the palette should store
 * @since 1.0
 * @see ChunkSection
 */
public interface ChunkPalette<E> {
    /**
     * Gets {@linkplain List a list} of elements, which this chunk palette stores. The list is represented in a format
     * specified in {@linkplain ChunkPalette a header javadoc of this class}.
     *
     * @return the list
     * @since 1.0
     */
    @NonNull List<E> elements();

    /**
     * Gets {@linkplain Set a set}, which contains unique values used by this chunk palette.
     *
     * @return the set
     * @since 1.0
     */
    @NonNull Set<E> uniqueElements();

    /**
     * Gets a number of times that an element specified is being used by this chunk palette.
     *
     * @param element the element
     * @return the number of times
     * @since 1.0
     */
    short elementCount(@NonNull E element);
}