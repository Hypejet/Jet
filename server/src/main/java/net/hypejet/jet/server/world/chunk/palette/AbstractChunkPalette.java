package net.hypejet.jet.server.world.chunk.palette;

import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import net.hypejet.jet.server.util.storage.BitStorage;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.world.chunk.section.ChunkPalette;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an abstract implementation of {@linkplain ChunkPalette a chunk palette}.
 *
 * @param <E> a type of elements that the palette should store
 * @since 1.0
 * @see ChunkPalette
 */
public sealed abstract class AbstractChunkPalette<E> implements ChunkPalette<E>
        permits DirectChunkPalette, IndirectChunkPalette, SingleValuedChunkPalette {

    private final byte bitsPerElement;
    private final long[] data;

    private final ChunkPaletteType type;
    private final IndexSpecification<E> indexSpecification;

    /**
     * Constructs the {@linkplain AbstractChunkPalette chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the should use
     * @param type a type of which the palette should be
     * @param data an array of a bit storage of data that the chunk palette should have
     * @param indexSpecification an index specification specifying registry indices
     *                           for elements of the constructed palette
     * @since 1.0
     */
    protected AbstractChunkPalette(byte bitsPerElement, @NonNull ChunkPaletteType type,
                                   long @NonNull [] data, @NonNull IndexSpecification<E> indexSpecification) {
        this.bitsPerElement = bitsPerElement;
        this.type = Objects.requireNonNull(type, "type");
        this.data = Objects.requireNonNull(data, "data");
        this.indexSpecification = Objects.requireNonNull(indexSpecification, "index specification");
    }

    @Override
    public final @NonNull List<E> elements() {
        List<E> elements = new ArrayList<>();
        for (int elementRegistryIndex : this.createElementArray())
            elements.add(this.indexSpecification.valueByIndex(elementRegistryIndex));
        return List.copyOf(elements);
    }

    @Override
    public final @NonNull Set<E> uniqueElements() {
        return this.elementCountMap().keySet();
    }

    @Override
    public final short elementCount(@NonNull E element) {
        return this.elementCountMap().getShort(element);
    }

    /**
     * Gets number of bits that each element of the data uses.
     *
     * @return the number of bits
     * @since 1.0
     */
    public final byte bitsPerElement() {
        return this.bitsPerElement;
    }

    /**
     * Gets an array of {@linkplain BitStorage bit storage} of data of this {@linkplain AbstractChunkPalette chunk palette}.
     *
     * @return the bit storage
     * @since 1.0
     */
    public final long @NonNull [] data() {
        return this.data;
    }

    /**
     * Gets {@linkplain ChunkPaletteType a type} of this chunk palette.
     *
     * @return the type
     * @since 1.0
     */
    public final @NonNull ChunkPaletteType type() {
        return this.type;
    }

    /**
     * Gets an {@linkplain IndexSpecification index specification} specifying registry indices
     * for elements of this palette.
     *
     * @return the index specification
     * @since 1.0
     */
    public final @NonNull IndexSpecification<E> indexSpecification() {
        return this.indexSpecification;
    }

    /**
     * Creates a copy of this {@linkplain AbstractChunkPalette chunk palette} with
     * {@linkplain ChunkPaletteUpdate chunk palette updates} specified applied.
     *
     * @param updates the updates
     * @return the new chunk palette
     * @since 1.0
     */
    @Contract(pure = true)
    public final @NonNull AbstractChunkPalette<E> withUpdates(@NonNull Collection<ChunkPaletteUpdate<E>> updates) {
        if (updates.isEmpty())
            return this;

        ChunkPaletteType type = this.type();

        int[] elements = this.createElementArray();
        Object2ShortMap<E> elementCountMap = new Object2ShortOpenHashMap<>(this.elementCountMap());

        boolean paletteChanged = false;
        for (ChunkPaletteUpdate<E> update : updates) {
            int elementIndex = calculateElementIndex(update.position());
            int previousElementRegistryIndex = elements[elementIndex];

            E newElement = update.newElement();
            int newElementRegistryIndex = this.indexSpecification.indexFor(newElement);

            if (previousElementRegistryIndex == newElementRegistryIndex) continue;
            if (!paletteChanged) paletteChanged = true;

            elements[elementIndex] = newElementRegistryIndex;
            incrementValue(elementCountMap, newElement);

            E previousElement = this.indexSpecification.valueByIndex(previousElementRegistryIndex);
            if (!elementCountMap.containsKey(previousElement))
                continue;

            short decreasedValue = (short) (elementCountMap.getShort(previousElement) - 1);
            if (decreasedValue <= 0) {
                elementCountMap.removeShort(previousElement);
                continue;
            }

            elementCountMap.put(previousElement, decreasedValue);
        }

        if (!paletteChanged) return this;
        return create(type, this.indexSpecification, elementCountMap, elements);
    }

    /**
     * Gets an element of the palette, which is present
     * at {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified.
     *
     * @param position the chunk-palette-relative position
     * @return the element
     * @since 1.0
     */
    public abstract @NonNull E getElement(@NonNull ChunkPaletteRelativePosition position);

    /**
     * Gets {@linkplain Object2ShortMap a map}, which maps elements to their count in the palette.
     *
     * @return the map
     * @since 1.0
     */
    public abstract @NonNull Object2ShortMap<E> elementCountMap();

    /**
     * Creates an array containing registry indices of elements of this palette.
     *
     * @return the array
     * @since 1.0
     */
    protected abstract int @NonNull [] createElementArray();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractChunkPalette<?> otherPalette)) return false;
        return Objects.equals(this.bitsPerElement, otherPalette.bitsPerElement)
                && Arrays.equals(this.data, otherPalette.data)
                && this.type == otherPalette.type
                && Objects.equals(this.indexSpecification, otherPalette.indexSpecification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bitsPerElement, Arrays.hashCode(this.data), this.type, this.indexSpecification);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "bitsPerElement=" + this.bitsPerElement +
                ", type=" + this.type +
                ", indexSpecification=" + this.indexSpecification +
                '}';
    }


    /**
     * Creates {@linkplain AbstractChunkPalette a chunk palette} for elements specified.
     *
     * @param type a type of which the chunk palette should have
     * @param indexSpecification an index specification specifying registry indices
     *                           for elements of the palette that is being created
     * @param elements elements that the chunk palette should have
     * @return the chunk palette
     * @param <E> a type of the elements
     * @since 1.0
     */
    @Contract(pure = true)
    public static <E> @NonNull AbstractChunkPalette<E> create(@NonNull ChunkPaletteType type,
                                                              @NonNull IndexSpecification<E> indexSpecification,
                                                              @NonNull List<E> elements) {
        int[] elementRegistryIndices = new int[elements.size()];
        for (int index = 0; index < elementRegistryIndices.length; index++) {
            E element = elements.get(index);
            int elementRegistryIndex = indexSpecification.indexFor(element);
            elementRegistryIndices[index] = elementRegistryIndex;
        }

        return create(type, indexSpecification, createCountMap(elements), elementRegistryIndices);
    }

    /**
     * Calculates index that an element
     * with {@linkplain ChunkPaletteRelativePosition a chunk-palette-relative position} specified is stored at.
     *
     * @param position the position
     * @return the index
     * @since 1.0
     */
    public static int calculateElementIndex(@NonNull ChunkPaletteRelativePosition position) {
        byte axisLength = position.paletteType().axisLength();
        return position.x() + (axisLength * position.z()) + (axisLength * axisLength * position.y());
    }

    /**
     * Creates {@linkplain Object2ShortMap a map}, which maps elements to their count in the element list specified.
     *
     * @param elements the element list
     * @return the map
     * @param <E> a type of elements of the element list
     * @since 1.0
     */
    protected static <E> @NonNull Object2ShortMap<E> createCountMap(@NonNull List<E> elements) {
        Object2ShortMap<E> elementCountMap = new Object2ShortOpenHashMap<>();
        for (E element : elements)
            incrementValue(elementCountMap, element);
        return elementCountMap;
    }

    private static <E> @NonNull AbstractChunkPalette<E> create(@NonNull ChunkPaletteType type,
                                                               @NonNull IndexSpecification<E> indexSpecification,
                                                               @NonNull Object2ShortMap<E> elementCountMap,
                                                               int @NonNull [] elements) {
        if (elementCountMap.size() == 1) {
            E onlyElement = Iterables.getOnlyElement(elementCountMap.keySet());
            return new SingleValuedChunkPalette<>(type, onlyElement, indexSpecification);
        }

        IndirectChunkPalette<E> indirectPalette = IndirectChunkPalette.createOrNull(
                type, elements, elementCountMap,
                indexSpecification
        );

        if (indirectPalette != null)
            return indirectPalette;
        return DirectChunkPalette.create(type, elements, indexSpecification, elementCountMap);
    }

    private static <K> void incrementValue(@NonNull Object2ShortMap<K> map, @NonNull K key) {
        short newCount = 1;
        if (map.containsKey(key))
            newCount += map.getShort(key);
        map.put(key, newCount);
    }

    /**
     * An index specification of values with the specified type.
     *
     * @param <V> the type of values that this index specification specifies indices for
     * @since 1.0
     */
    // TODO: Require providing registries as arguments instead
    public interface IndexSpecification<V> {
        /**
         * Gets an index of the specified value.
         *
         * @param value the value
         * @return the index
         * @throws IllegalArgumentException if this index specification does not provide an index
         *                                  for the specified value
         * @since 1.0
         */
        int indexFor(@NonNull V value);

        /**
         * Gets a value that is bound to the specified index.
         *
         * @param index the index
         * @return the value
         * @throws IllegalArgumentException if this index specification does not provide a value
         *                                  for the specified index
         * @since 1.0
         */
        @NonNull V valueByIndex(int index);
    }
}