package net.hypejet.jet.server.world.chunk.palette;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.function.IntResultingFunction;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.util.array.UnmodifiableIntegerArray;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Map<E, Integer> elementCountMap;

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
     * @param elementToIdentifierFunction a function, which should represent elements of the palette as integers
     * @since 1.0
     */
    private IndirectChunkPalette(byte bitsPerElement, @NonNull ChunkPaletteType type, long @NonNull [] data,
                                 @NonNull List<E> elements, int @NonNull [] registryIndices,
                                 @NonNull Map<E, Integer> elementCountMap,
                                 @NonNull IntResultingFunction<E> elementToIdentifierFunction) {
        super(bitsPerElement, type, data, elementToIdentifierFunction);

        this.elements = List.copyOf(NullabilityUtil.requireNonNull(elements, "elements"));
        this.elementCountMap = Map.copyOf(NullabilityUtil.requireNonNull(elementCountMap, "element count map"));

        this.registryIndices = new UnmodifiableIntegerArray(NullabilityUtil.requireNonNull(
                registryIndices, "registry indices"
        ));
    }

    @Override
    public @NonNull E getElement(byte x, byte y, byte z) {
        int elementIndex = ChunkPalette.calculateElementIndex(this.type().axisLength(), x, y, z);

        E element = this.elements.get(elementIndex);
        if (element == null)
            throw new IllegalArgumentException(String.format("Invalid element index: %d", elementIndex));

        return element;
    }

    @Override
    protected @NonNull List<E> createElementList() {
        return this.elements;
    }

    @Override
    protected @NonNull Map<E, Integer> createElementCountMap() {
        return this.elementCountMap;
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
     * @param elementToIdentifierFunction a function, which should represent elements of the palette as integers
     * @return the indirect chunk palette
     * @since 1.0
     * @throws IllegalArgumentException if the bits-per-element value calculated exceeds the maximum allowed value for
     *                                  an indirect palette
     */
    public static <E> @NonNull IndirectChunkPalette<E> create(
            @NonNull ChunkPaletteType type, @NonNull List<E> elements,
            @NonNull IntResultingFunction<E> elementToIdentifierFunction
    ) {
        IndirectChunkPalette<E> palette = createOrNull(
                type, elements, ChunkPalette.createCountMap(elements),
                elementToIdentifierFunction
        );

        if (palette == null) {
            throw new IllegalArgumentException(
                    "The indirect palette is not eligible for the element list specified, since the bits-per-element" +
                            " value calculated exceeds the maximum allowed value for an indirect palette"
            );
        }

        return palette;
    }

    /**
     * Creates {@linkplain IndirectChunkPalette an indirect chunk palette}.
     *
     * @param type a type of which the palette should be
     * @param elements elements that should be put to the data array
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @param elementToIdentifierFunction a function, which should represent elements of the palette as integers
     * @return the indirect chunk palette, {@code null} if the bits-per-element value calculated exceeds the maximum
     *         allowed value for an indirect palette
     * @since 1.0
     */
    static <E> @Nullable IndirectChunkPalette<E> createOrNull(
            @NonNull ChunkPaletteType type, @NonNull List<E> elements,
            @NonNull Map<E, Integer> elementCountMap, @NonNull IntResultingFunction<E> elementToIdentifierFunction
    ) {
        NullabilityUtil.requireNonNull(type, "type");
        NullabilityUtil.requireNonNull(elements, "elements");
        NullabilityUtil.requireNonNull(elementCountMap, "element count map");
        NullabilityUtil.requireNonNull(elementToIdentifierFunction, "element to identifier function");

        /* Maximum bit count used by any element of indirect chunk palette is a bit count of the last index of
           the registry indices array. */
        byte bitsPerElement = (byte) MathUtil.bitCount(elementCountMap.size() - 1);
        if (bitsPerElement > type.maximumIndirectBits())
            return null;

        bitsPerElement = (byte) Math.max(type.minimumIndirectBits(), bitsPerElement);

        int nextRegistryIndicesArrayIndex = 0;
        Map<E, Integer> elementToRegistryIndicesArrayIndexMap = new HashMap<>();

        int[] dataElements = new int[elements.size()];
        for (int index = 0; index < dataElements.length; index++) {
            E element = elements.get(index);

            Integer stateIndex = elementToRegistryIndicesArrayIndexMap.get(element);
            if (stateIndex == null) {
                stateIndex = nextRegistryIndicesArrayIndex++;
                elementToRegistryIndicesArrayIndexMap.put(element, stateIndex);
            }

            dataElements[index] = stateIndex;
        }

        int[] registryIndices = new int[elementToRegistryIndicesArrayIndexMap.size()];
        for (Map.Entry<E, Integer> entry : elementToRegistryIndicesArrayIndexMap.entrySet())
            registryIndices[entry.getValue()] = elementToIdentifierFunction.apply(entry.getKey());

        return new IndirectChunkPalette<>(
                bitsPerElement, type,
                ChunkPalette.createDataArray(bitsPerElement, dataElements, type),
                elements, registryIndices, elementCountMap, elementToIdentifierFunction
        );
    }
}