package net.hypejet.jet.server.world.chunk.palette;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortMaps;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenCustomHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.hash.IdentityHashStrategy;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.util.array.UnmodifiableIntegerArray;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Represents {@linkplain ChunkPalette a chunk palette}, which contains an array of identifiers of elements of registry
 * associated with the palette and whose data is indices into that array.
 *
 * @since 1.0
 * @param <E> a type of elements that the palette stores
 * @see ChunkPalette
 */
public final class IndirectChunkPalette<E> extends ChunkPalette<E> {

    private final List<E> elements;
    private final Object2ShortMap<E> elementCountMap;

    private final UnmodifiableIntegerArray registryIndices;

    /**
     * Constructs the {@linkplain IndirectChunkPalette indirect chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param type a type of which the palette should be
     * @param data data that the chunk palette should have
     * @param elements elements that should be put to the data array
     * @param registryIndices the array of identifiers of registry associated with this palette, which are used
     *                        in the data
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @param elementOrder an element order, from which identifiers of elements of the palette should be retrieved
     * @since 1.0
     */
    private IndirectChunkPalette(byte bitsPerElement, @NonNull ChunkPaletteType type, long @NonNull [] data,
                                 @NonNull List<E> elements, int @NonNull [] registryIndices,
                                 @NonNull Object2ShortMap<E> elementCountMap,
                                 @NonNull ElementOrder<E> elementOrder) {
        super(bitsPerElement, type, data, elementOrder);

        this.elements = List.copyOf(NullabilityUtil.requireNonNull(elements, "elements"));
        this.elementCountMap = Object2ShortMaps.unmodifiable(
                new Object2ShortOpenCustomHashMap<>(
                        NullabilityUtil.requireNonNull(elementCountMap, "element count map"),
                        IdentityHashStrategy.INSTANCE
                )
        );

        this.registryIndices = new UnmodifiableIntegerArray(NullabilityUtil.requireNonNull(
                registryIndices, "registry indices"
        ));
    }

    @Override
    public @NonNull E getElement(@NonNull ChunkPaletteRelativePosition position) {
        ChunkPaletteType positionPaletteType = position.paletteType();
        ChunkPaletteType expectedPaletteType = this.type();

        if (positionPaletteType != expectedPaletteType) {
            throw new IllegalArgumentException(String.format(
                    "The chunk-palette-relative position specified has been created for chunk palettes with" +
                            " type of %s, not %s",
                    positionPaletteType, expectedPaletteType
            ));
        }

        int elementIndex = ChunkPalette.calculateElementIndex(position);

        E element = this.elements.get(elementIndex);
        if (element == null)
            throw new IllegalArgumentException(String.format("Invalid element index: %d", elementIndex));

        return element;
    }

    @Override
    public @NonNull Object2ShortMap<E> elementCountMap() {
        return this.elementCountMap;
    }

    @Override
    protected @NonNull List<E> createElementList() {
        return this.elements;
    }

    /**
     * Gets an array of identifiers of registry elements that data of this palette uses.
     *
     * @return the array
     * @since 1.0
     */
    public int @NonNull [] registryIndices() {
        return this.registryIndices.array();
    }

    /**
     * Creates {@linkplain IndirectChunkPalette an indirect chunk palette}.
     *
     * @param type a type of which the palette should be
     * @param elements elements that should be put to the data array
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @param elementOrder an element order, from which identifiers of elements of the palette should be retrieved
     * @return the indirect chunk palette, {@code null} if the bits-per-element value calculated exceeds the maximum
     *         allowed value for an indirect palette
     * @since 1.0
     */
    static <E> @Nullable IndirectChunkPalette<E> createOrNull(
            @NonNull ChunkPaletteType type, @NonNull List<E> elements,
            @NonNull Object2ShortMap<E> elementCountMap, @NonNull ElementOrder<E> elementOrder
    ) {
        NullabilityUtil.requireNonNull(type, "type");
        NullabilityUtil.requireNonNull(elements, "elements");
        NullabilityUtil.requireNonNull(elementCountMap, "element count map");
        NullabilityUtil.requireNonNull(elementOrder, "element order");

        /* Maximum bit count used by any element of indirect chunk palette is a bit count of the last index of
           the registry indices array. */
        byte bitsPerElement = (byte) MathUtil.bitCount(elementCountMap.size() - 1);
        if (bitsPerElement > type.maximumIndirectBits())
            return null;

        bitsPerElement = (byte) Math.max(type.minimumIndirectBits(), bitsPerElement);

        int nextRegistryIndicesArrayIndex = 0;
        Object2IntMap<E> elementToRegistryIndicesArrayIndexMap = new Object2IntOpenCustomHashMap<>(
                IdentityHashStrategy.INSTANCE
        );

        int[] dataElements = new int[elements.size()];
        for (int index = 0; index < dataElements.length; index++) {
            E element = elements.get(index);

            int stateIndex;
            if (elementToRegistryIndicesArrayIndexMap.containsKey(element)) {
                stateIndex = elementToRegistryIndicesArrayIndexMap.getInt(element);
            } else {
                stateIndex = nextRegistryIndicesArrayIndex++;
                elementToRegistryIndicesArrayIndexMap.put(element, stateIndex);
            }

            dataElements[index] = stateIndex;
        }

        int[] registryIndices = new int[elementToRegistryIndicesArrayIndexMap.size()];
        for (Object2IntMap.Entry<E> entry : elementToRegistryIndicesArrayIndexMap.object2IntEntrySet())
            registryIndices[entry.getIntValue()] = elementOrder.identifierOf(entry.getKey());

        return new IndirectChunkPalette<>(
                bitsPerElement, type,
                ChunkPalette.createDataArray(bitsPerElement, dataElements, type),
                elements, registryIndices, elementCountMap, elementOrder
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndirectChunkPalette<?> otherPalette)) return false;
        return Objects.equals(this.elements, otherPalette.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.elements);
    }

    @Override
    public String toString() {
        return "IndirectChunkPalette{" +
                "elements=" + this.elements +
                '}';
    }
}