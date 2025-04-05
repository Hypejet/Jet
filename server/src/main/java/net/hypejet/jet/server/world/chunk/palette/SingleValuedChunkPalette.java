package net.hypejet.jet.server.world.chunk.palette;

import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortMaps;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenCustomHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.hash.IdentityHashStrategy;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

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
    private final int elementIdentifier;

    private final Object2ShortMap<E> elementCountMap;

    /**
     * Constructs the {@linkplain SingleValuedChunkPalette single-valued chunk palette}.
     *
     * @param type a type of which the palette should be
     * @param element the element
     * @param elementOrder an element order, from which identifiers of elements of the palette should be retrieved
     * @since 1.0
     */
    public SingleValuedChunkPalette(@NonNull ChunkPaletteType type, @NonNull E element,
                                    @NonNull ElementOrder<? extends E> elementOrder) {
        super(BITS_PER_ELEMENT, type, EMPTY_DATA, elementOrder);

        this.element = NullabilityUtil.requireNonNull(element, "element");
        this.elementIdentifier = elementOrder.identifierOf(element);

        Object2ShortMap<E> elementCountMap = new Object2ShortOpenCustomHashMap<>(IdentityHashStrategy.INSTANCE);
        elementCountMap.put(element, type.elementCount());
        this.elementCountMap = Object2ShortMaps.unmodifiable(elementCountMap);
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
        Arrays.fill(elements, this.elementIdentifier);
        return elements;
    }

    /**
     * Gets an identifier of the element that this chunk palette returns for each position.
     *
     * @return the element identifier
     * @since 1.0
     */
    public int elementIdentifier() {
        return this.elementIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SingleValuedChunkPalette<?> otherPalette)) return false;
        if (!super.equals(o)) return false;
        return this.elementIdentifier == otherPalette.elementIdentifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.elementIdentifier);
    }

    @Override
    public String toString() {
        return "SingleValuedChunkPalette{" +
                "element=" + this.element +
                '}';
    }
}