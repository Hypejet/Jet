package net.hypejet.jet.server.world.chunk.palette;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortMaps;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.util.storage.BitStorage;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import net.hypejet.jet.util.array.UnmodifiableIntegerArray;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Represents {@linkplain AbstractChunkPalette a chunk palette} with an additional array storing registry indices
 * of elements used by the chunk palette while the main data array references to indices of the additional array.
 *
 * @since 1.0
 * @param <E> a type of elements that the palette stores
 * @see AbstractChunkPalette
 */
public final class IndirectChunkPalette<E> extends AbstractChunkPalette<E> {

    private final BitStorage storage;

    private final Object2ShortMap<E> elementCountMap;
    private final UnmodifiableIntegerArray registryIndices;

    /**
     * Constructs the {@linkplain IndirectChunkPalette indirect chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param type a type of which the palette should be
     * @param storage a bit storage storing indices to the additional array that reference
     *                to elements that the chunk palette should contain
     * @param registryIndices the additional array storing registry indices of elements
     *                        that are going to be used in the constructed palette
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @param indexSpecification an index specification specifying registry indices
     *                           for elements of the constructed palette
     * @since 1.0
     */
    private IndirectChunkPalette(byte bitsPerElement, @NonNull ChunkPaletteType type, @NonNull BitStorage storage,
                                 int @NonNull [] registryIndices, @NonNull Object2ShortMap<E> elementCountMap,
                                 @NonNull IndexSpecification<E> indexSpecification) {
        super(bitsPerElement, type, Objects.requireNonNull(storage, "storage").data(), indexSpecification);

        Objects.requireNonNull(elementCountMap, "element count map");
        Objects.requireNonNull(registryIndices, "registry indices");

        this.storage = storage;
        this.elementCountMap = Object2ShortMaps.unmodifiable(new Object2ShortOpenHashMap<>(elementCountMap));
        this.registryIndices = new UnmodifiableIntegerArray(registryIndices);
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

        int elementIndex = AbstractChunkPalette.calculateElementIndex(position);
        int elementRegistryIndex = this.registryIndices.array()[this.storage.getElement(elementIndex)];
        return this.indexSpecification().valueByIndex(elementRegistryIndex);
    }

    @Override
    public @NonNull Object2ShortMap<E> elementCountMap() {
        return this.elementCountMap;
    }

    @Override
    protected int @NonNull [] createElementArray() {
        int[] unpackedData = this.storage.unpack();
        int[] registryIndices = this.registryIndices.array();

        int[] elements = new int[unpackedData.length];
        for (int index = 0; index < elements.length; index++) {
            int elementRegistryIndex = registryIndices[unpackedData[index]];
            elements[index] = elementRegistryIndex;
        }

        return elements;
    }

    /**
     * Gets the additional array storing registry indices of elements used by this chunk palette.
     *
     * @return the additional array
     * @since 1.0
     */
    public int @NonNull [] registryIndices() {
        return this.registryIndices.array();
    }

    /**
     * Creates {@linkplain IndirectChunkPalette an indirect chunk palette}.
     *
     * @param type a type of which the palette should be
     * @param elements registry indices of elements that should be put to the data array
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @param indexSpecification an index specification specifying registry indices for elements of the created palette
     * @return the indirect chunk palette, {@code null} if the bits-per-element value calculated exceeds the maximum
     *         allowed value for an indirect palette
     * @since 1.0
     */
    static <E> @Nullable IndirectChunkPalette<E> createOrNull(
            @NonNull ChunkPaletteType type, int @NonNull [] elements,
            @NonNull Object2ShortMap<E> elementCountMap, @NonNull IndexSpecification<E> indexSpecification
    ) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(elements, "elements");
        Objects.requireNonNull(elementCountMap, "element count map");
        Objects.requireNonNull(indexSpecification, "index specification");

        /* Maximum bit count used by any element of indirect chunk palette is a bit count of the last index of
           the registry indices array. */
        byte bitsPerElement = (byte) MathUtil.bitCount(elementCountMap.size() - 1);
        if (bitsPerElement > type.maximumIndirectBits())
            return null;

        bitsPerElement = (byte) Math.max(type.minimumIndirectBits(), bitsPerElement);

        int nextRegistryIndicesArrayIndex = 0;
        Int2IntMap elementToRegistryIndicesArrayIndexMap = new Int2IntOpenHashMap();

        int[] dataElements = new int[elements.length];
        for (int index = 0; index < dataElements.length; index++) {
            int elementRegistryIndex = elements[index];

            int stateIndex;
            if (elementToRegistryIndicesArrayIndexMap.containsKey(elementRegistryIndex)) {
                stateIndex = elementToRegistryIndicesArrayIndexMap.get(elementRegistryIndex);
            } else {
                stateIndex = nextRegistryIndicesArrayIndex++;
                elementToRegistryIndicesArrayIndexMap.put(elementRegistryIndex, stateIndex);
            }

            dataElements[index] = stateIndex;
        }

        int[] registryIndices = new int[elementToRegistryIndicesArrayIndexMap.size()];
        for (Int2IntMap.Entry entry : elementToRegistryIndicesArrayIndexMap.int2IntEntrySet())
            registryIndices[entry.getIntValue()] = entry.getIntKey();

        return new IndirectChunkPalette<>(
                bitsPerElement, type,
                new BitStorage(bitsPerElement, dataElements),
                registryIndices, elementCountMap, indexSpecification
        );
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IndirectChunkPalette<?> otherPalette)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.registryIndices, otherPalette.registryIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.elementCountMap, this.registryIndices);
    }
}