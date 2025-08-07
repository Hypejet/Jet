package net.hypejet.jet.server.world.chunk.palette;

import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortMaps;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents {@linkplain AbstractChunkPalette a chunk palette}, which contains the same element at each position.
 *
 * @since 1.0
 * @param <E> a type of elements that the palette stores
 * @see AbstractChunkPalette
 */
public final class SingleValuedChunkPalette<E> extends AbstractChunkPalette<E> {

    private static final byte BITS_PER_ELEMENT = 0;
    private static final long[] EMPTY_DATA = new long[0];

    private final E element;
    private final int elementRegistryIndex;

    private final Object2ShortMap<E> elementCountMap;

    /**
     * Constructs the {@linkplain SingleValuedChunkPalette single-valued chunk palette}.
     *
     * @param type a type of which the palette should be
     * @param element the element
     * @param indexSpecification an index specification specifying registry index
     *                           for the only element of the constructed palette
     * @since 1.0
     */
    public SingleValuedChunkPalette(@NonNull ChunkPaletteType type, @NonNull E element,
                                    @NonNull IndexSpecification<E> indexSpecification) {
        super(BITS_PER_ELEMENT, type, EMPTY_DATA, indexSpecification);
        this.element = Objects.requireNonNull(element, "element");
        this.elementRegistryIndex = indexSpecification.indexFor(element);
        this.elementCountMap = Object2ShortMaps.singleton(element, type.elementCount());
    }

    @Override
    public @NonNull E getElement(@NonNull ChunkPaletteRelativePosition position) {
        return this.element;
    }

    @Override
    public @NonNull Object2ShortMap<E> elementCountMap() {
        return this.elementCountMap;
    }

    @Override
    protected int @NonNull [] createElementArray() {
        int[] elements = new int[this.type().elementCount()];
        Arrays.fill(elements, this.elementRegistryIndex);
        return elements;
    }

    /**
     * Gets the element that this chunk palette returns for each position.
     *
     * @return the element
     * @since 1.0
     */
    public @NonNull E element() {
        return this.element;
    }

    /**
     * Gets a registry index of the element that this chunk palette returns for each position.
     *
     * @return the element registry index
     * @since 1.0
     */
    public int elementRegistryIndex() {
        return this.elementRegistryIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SingleValuedChunkPalette<?> otherPalette)) return false;
        if (!super.equals(o)) return false;
        return this.elementRegistryIndex == otherPalette.elementRegistryIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.elementRegistryIndex);
    }

    @Override
    public String toString() {
        return "SingleValuedChunkPalette{" +
                "element=" + this.element +
                '}';
    }
}