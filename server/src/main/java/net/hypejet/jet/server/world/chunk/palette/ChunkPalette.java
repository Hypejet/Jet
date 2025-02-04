package net.hypejet.jet.server.world.chunk.palette;

import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenCustomHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.array.UnmodifiableLongArray;
import net.hypejet.jet.server.util.hash.IdentityHashStrategy;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.server.world.coordinate.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a data container of {@linkplain net.hypejet.jet.server.world.chunk.section.ChunkSection a chunk section}.
 *
 * @since 1.0
 * @param <E> a type of elements that the palette stores
 * @see net.hypejet.jet.server.world.chunk.section.ChunkSection
 */
public sealed abstract class ChunkPalette<E> permits DirectChunkPalette, IndirectChunkPalette,
        SingleValuedChunkPalette {

    private final byte bitsPerElement;
    private final UnmodifiableLongArray data;

    private final ChunkPaletteType type;
    private final ElementOrder<E> elementOrder;

    /**
     * Constructs the {@linkplain ChunkPalette chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the should use
     * @param type a type of which the palette should be
     * @param data the data as a long array, where one long can store multiple elements, number of elements that one
     *             long can have depends on the previous bits-per-element value
     * @param elementOrder an element order, from which identifiers of elements of the palette should be retrieved
     * @since 1.0
     */
    protected ChunkPalette(byte bitsPerElement, @NonNull ChunkPaletteType type, long @NonNull [] data,
                           @NonNull ElementOrder<E> elementOrder) {
        this.bitsPerElement = bitsPerElement;
        this.type = NullabilityUtil.requireNonNull(type, "type");
        this.data = new UnmodifiableLongArray(NullabilityUtil.requireNonNull(data, "data"));
        this.elementOrder = NullabilityUtil.requireNonNull(elementOrder, "element order");
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
     * Gets the data as a long array, where one long can store multiple elements. Number of elements that one long can
     * have depends on the {@linkplain #bitsPerElement() bits-per-element} value. Note that the array does not need
     * to store element values directly.
     *
     * @return the data
     * @since 1.0
     */
    public final long @NonNull [] data() {
        return this.data.array();
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
     * Gets {@linkplain ElementOrder an element order}, from which identifiers of elements of this palette
     * are retrieved.
     *
     * @return the element order
     * @since 1.0
     */
    public final @NonNull ElementOrder<E> elementToIdentifierFunction() {
        return this.elementOrder;
    }

    /**
     * Creates a copy of this {@linkplain ChunkPalette chunk palette} with
     * {@linkplain ChunkPaletteUpdate chunk palette updates} specified applied.
     *
     * @param updates the updates
     * @return the new chunk palette
     * @since 1.0
     */
    public final @NonNull ChunkPalette<E> withUpdates(@NonNull Collection<ChunkPaletteUpdate<E>> updates) {
        if (updates.isEmpty())
            return this;

        ChunkPaletteType type = this.type();

        List<E> elements = new ArrayList<>(this.createElementList());
        Object2ShortMap<E> elementCountMap = new Object2ShortOpenCustomHashMap<>(
                this.elementCountMap(),
                IdentityHashStrategy.INSTANCE
        );

        boolean paletteChanged = false;
        for (ChunkPaletteUpdate<E> update : updates) {
            int elementIndex = calculateElementIndex(update.position());

            E previousElement = elements.get(elementIndex);
            E newElement = update.newElement();

            if (previousElement == newElement) continue;
            if (!paletteChanged) paletteChanged = true;

            elements.set(elementIndex, newElement);
            incrementValue(elementCountMap, newElement);

            if (!elementCountMap.containsKey(previousElement))
                continue;

            short decreasedValue = (short) (elementCountMap.getShort(previousElement) - 1);
            if (decreasedValue <= 0) {
                elementCountMap.removeShort(previousElement);
                continue;
            }

            elementCountMap.put(previousElement, decreasedValue);
        }

        if (!paletteChanged)
            return this;

        return create(type, this.elementOrder, elementCountMap, elements);
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
     * Creates {@linkplain List a list} of elements of this palette.
     *
     * @return the list
     * @since 1.0
     */
    protected abstract @NonNull List<E> createElementList();

    /**
     * Creates {@linkplain ChunkPalette a chunk palette} for elements specified.
     *
     * @param type a type of which the chunk palette should have
     * @param elementOrder an element order, from which identifiers of elements of the palette should be retrieved
     * @param elements elements that the chunk palette should have
     * @return the chunk palette
     * @param <E> a type of the elements
     * @since 1.0
     */
    public static <E> @NonNull ChunkPalette<E> create(@NonNull ChunkPaletteType type,
                                                      @NonNull ElementOrder<E> elementOrder,
                                                      @NonNull List<E> elements) {
        return create(type, elementOrder, createCountMap(elements), elements);
    }

    /**
     * Calculates index of that an element
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
     * Creates a long array containing elements specified, where one long can store multiple elements. Number of
     * elements that one long can have depends on the bits-per-element value specified.
     *
     * @param bitsPerElement the bits-per-element value
     * @param elements the elements
     * @param type a type of chunk palette that the data array is created for
     * @return the long array
     * @since 1.0
     * @throws IllegalArgumentException if the length of elements specified is invalid or count of bits for one of
     *                                  elements specified is higher than bits-per-element value specified
     */
    protected static long @NonNull [] createDataArray(byte bitsPerElement, int @NonNull [] elements,
                                                      @NonNull ChunkPaletteType type) {
        int expectedElementLength = type.elementCount();
        int elementsLength = elements.length;

        if (expectedElementLength != elementsLength) {
            throw new IllegalArgumentException(String.format(
                    "The length of elements specified (%d) for a chunk palette type specified (%s) must be %d",
                    elementsLength, type, expectedElementLength
            ));
        }

        byte elementsPerLong = (byte) (Math.floorDiv(Long.SIZE, bitsPerElement));
        long[] values = new long[Math.ceilDiv(elements.length, elementsPerLong)];

        int valueIndex = 0;

        long value = 0L;
        byte bitShift = 0;

        for (int index = 0; index < elements.length; index++) {
            int element = elements[index];
            int elementBits = MathUtil.bitCount(element);

            if (elementBits > bitsPerElement) {
                throw new IllegalArgumentException(String.format(
                        "Count of bits of element with index of %s is higher than bits per element (%s>%s)",
                        index, elementBits, bitsPerElement
                ));
            }

            long bitwiseArgument = (long) element << bitShift;
            value |= bitwiseArgument;

            bitShift += bitsPerElement;

            int elementNumber = (index + 1) - (valueIndex * elementsPerLong);
            if (elementNumber >= elementsPerLong) {
                values[valueIndex] = value;

                valueIndex++;

                value = 0L;
                bitShift = 0;
            }
        }

        return values;
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
        Object2ShortMap<E> elementCountMap = new Object2ShortOpenCustomHashMap<>(IdentityHashStrategy.INSTANCE);
        for (E element : elements)
            incrementValue(elementCountMap, element);
        return elementCountMap;
    }

    private static <E> @NonNull ChunkPalette<E> create(@NonNull ChunkPaletteType type,
                                                       @NonNull ElementOrder<E> elementOrder,
                                                       @NonNull Object2ShortMap<E> elementCountMap,
                                                       @NonNull List<E> elements) {
        if (elementCountMap.size() == 1) {
            E onlyElement = Iterables.getOnlyElement(elementCountMap.keySet());
            return new SingleValuedChunkPalette<>(type, onlyElement, elementOrder);
        }

        IndirectChunkPalette<E> indirectPalette = IndirectChunkPalette.createOrNull(
                type, elements, elementCountMap,
                elementOrder
        );

        if (indirectPalette != null)
            return indirectPalette;
        return DirectChunkPalette.create(type, elements, elementOrder, elementCountMap);
    }

    private static <K> void incrementValue(@NonNull Object2ShortMap<K> map, @NonNull K key) {
        short newCount = 1;
        if (map.containsKey(key))
            newCount += map.getShort(key);
        map.put(key, newCount);
    }
}