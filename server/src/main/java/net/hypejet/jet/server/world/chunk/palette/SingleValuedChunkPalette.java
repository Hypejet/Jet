package net.hypejet.jet.server.world.chunk.palette;

import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortMaps;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenCustomHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.hash.IdentityHashStrategy;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

/**
 * Represents {@linkplain ChunkPalette a chunk palette}, which contains the same element at each position.
 *
 * @since 1.0
 * @param <E> a type of elements that the palette stores
 * @see ChunkPalette
 */
public final class SingleValuedChunkPalette<E> extends ChunkPalette<E> {

    private static final long[] EMPTY_LONG_ARRAY = new long[0];

    private final E element;
    private final int elementIdentifier;

    private final Object2ShortMap<E> elementCountMap;

    /**
     * Constructs the {@linkplain SingleValuedChunkPalette single-valued chunk palette}.
     *
     * @param type a type of which the palette should be
     * @param element the element
     * @param elementToIdentifierFunction a function, which should represent elements of the palette as integers
     * @since 1.0
     */
    public SingleValuedChunkPalette(@NonNull ChunkPaletteType type, @NonNull E element,
                                    @NonNull ToIntFunction<E> elementToIdentifierFunction) {
        super((byte) 0, type, EMPTY_LONG_ARRAY, elementToIdentifierFunction);

        this.element = NullabilityUtil.requireNonNull(element, "element");
        this.elementIdentifier = elementToIdentifierFunction.applyAsInt(element);

        Object2ShortMap<E> elementCountMap = new Object2ShortOpenCustomHashMap<>(IdentityHashStrategy.INSTANCE);
        elementCountMap.put(element, type.elementCount());
        this.elementCountMap = Object2ShortMaps.unmodifiable(elementCountMap);
    }

    @Override
    public @NonNull E getElement(byte x, byte y, byte z) {
        return this.element;
    }

    @Override
    public @NonNull Object2ShortMap<E> elementCountMap() {
        return this.elementCountMap;
    }

    @Override
    protected @NonNull List<E> createElementList() {
        return Collections.nCopies(this.type().elementCount(), this.element);
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
        if (this == o) return true;
        if (!(o instanceof SingleValuedChunkPalette<?> otherPalette)) return false;
        return Objects.equals(this.element, otherPalette.element);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.element);
    }

    @Override
    public String toString() {
        return "SingleValuedChunkPalette{" +
                "element=" + this.element +
                '}';
    }
}