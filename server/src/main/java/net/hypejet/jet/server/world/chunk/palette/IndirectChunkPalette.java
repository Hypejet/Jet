package net.hypejet.jet.server.world.chunk.palette;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.util.array.UnmodifiableIntegerArray;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@linkplain ChunkPalette a chunk palette}, which contains an array of identifiers of elements of registry
 * associated with the palette and whose data is indices into that array.
 *
 * @since 1.0
 * @see ChunkPalette
 */
public final class IndirectChunkPalette extends ChunkPalette {

    private final byte axisLength;

    private final UnmodifiableIntegerArray elements;
    private final UnmodifiableIntegerArray registryIndices;

    /**
     * Constructs the {@linkplain IndirectChunkPalette indirect chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param axisLength length that each axis of the chunk palette should have
     * @param data data that the chunk palette should have
     * @param elements elements that should be put to the data array
     * @param registryIndices the array of identifiers of registry associated with this palette, which are used
     *                        in the data
     * @since 1.0
     */
    private IndirectChunkPalette(byte bitsPerElement, byte axisLength, long @NotNull [] data,
                                 int @NotNull [] elements, int @NotNull [] registryIndices) {
        super(bitsPerElement, NullabilityUtil.requireNonNull(data, "data"));
        this.axisLength = axisLength;
        this.elements = new UnmodifiableIntegerArray(NullabilityUtil.requireNonNull(elements, "elements"));

        this.registryIndices = new UnmodifiableIntegerArray(NullabilityUtil.requireNonNull(
                registryIndices, "registry indices"
        ));
    }

    @Override
    public int getElement(byte x, byte y, byte z) {
        return this.elements.array()[ChunkPalette.calculateElementIndex(this.axisLength, x, y, z)];
    }

    /**
     * Gets an array of identifiers of registry elements that data of this palette uses.
     *
     * @return the array
     * @since 1.0
     */
    public int @NotNull [] registryIndices() {
        return this.registryIndices.array();
    }

    /**
     * Creates {@linkplain IndirectChunkPalette an indirect chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param elements elements that should be put to the data array
     * @param axisLength length that each axis of the chunk palette should have
     * @return the indirect chunk palette
     * @since 1.0
     */
    /* TODO: | Replace the factory method with public constructor when flexible constructors get finally implemented
       TODO: | in Java */
    public static @NotNull IndirectChunkPalette create(byte bitsPerElement, byte axisLength,
                                                       int @NotNull [] elements) {
        NullabilityUtil.requireNonNull(elements, "elements");

        int nextRegistryIndicesArrayIndex = 0;
        IntObjectMap<Integer> elementToRegistryIndicesArrayIndexMap = new IntObjectHashMap<>();

        int[] dataElements = new int[elements.length];
        for (int index = 0; index < elements.length; index++) {
            int state = elements[index];

            Integer stateIndex = elementToRegistryIndicesArrayIndexMap.get(state);
            if (stateIndex == null) {
                stateIndex = nextRegistryIndicesArrayIndex++;
                elementToRegistryIndicesArrayIndexMap.put(state, stateIndex);
            }

            dataElements[index] = stateIndex;
        }

        int[] registryIndices = new int[elementToRegistryIndicesArrayIndexMap.size()];
        for (IntObjectMap.PrimitiveEntry<Integer> entry : elementToRegistryIndicesArrayIndexMap.entries())
            registryIndices[entry.value()] = entry.key();

        return new IndirectChunkPalette(
                bitsPerElement, axisLength,
                ChunkPalette.createDataArray(bitsPerElement, dataElements, axisLength),
                elements, registryIndices
        );
    }
}