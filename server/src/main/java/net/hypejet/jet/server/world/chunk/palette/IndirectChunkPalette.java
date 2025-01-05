package net.hypejet.jet.server.world.chunk.palette;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.server.world.chunk.palette.usage.ChunkPaletteUsageType;
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

    private final UnmodifiableIntegerArray elements;
    private final UnmodifiableIntegerArray registryIndices;

    /**
     * Constructs the {@linkplain IndirectChunkPalette indirect chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param usageType a type of usage that the palette is created for
     * @param data data that the chunk palette should have
     * @param elements elements that should be put to the data array
     * @param registryIndices the array of identifiers of registry associated with this palette, which are used
     *                        in the data
     * @since 1.0
     */
    private IndirectChunkPalette(byte bitsPerElement, @NotNull ChunkPaletteUsageType usageType, long @NotNull [] data,
                                 int @NotNull [] elements, int @NotNull [] registryIndices) {
        super(bitsPerElement, usageType, data);

        this.elements = new UnmodifiableIntegerArray(NullabilityUtil.requireNonNull(elements, "elements"));
        this.registryIndices = new UnmodifiableIntegerArray(NullabilityUtil.requireNonNull(
                registryIndices, "registry indices"
        ));
    }

    @Override
    public int getElement(byte x, byte y, byte z) {
        return this.elements.array()[ChunkPalette.calculateElementIndex(this.usageType().axisLength(), x, y, z)];
    }

    @Override
    public @NotNull ChunkPalette withUpdates(@NotNull ChunkPaletteUpdate @NotNull ... updates) {
        if (updates.length == 0)
            return this;

        int[] elements = this.elements.array();

        ChunkPaletteUsageType usageType = this.usageType();
        byte bitsPerElement = this.bitsPerElement();

        for (ChunkPaletteUpdate update : updates) {
            int elementIndex = ChunkPalette.calculateElementIndex(
                    update.sectionX(), update.sectionY(), update.sectionZ(), usageType.axisLength()
            );

            int newElement = update.newElement();
            elements[elementIndex] = newElement;
            bitsPerElement = MathUtil.max(bitsPerElement, (byte) MathUtil.ceilLog2(newElement));
        }

        // TODO: Consider downgrading if it is possible
        if (bitsPerElement > usageType.maximumIndirectBits())
            return DirectChunkPalette.create(bitsPerElement, usageType, elements);
        return IndirectChunkPalette.create(bitsPerElement, usageType, elements);
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
     * <p>If the bits-per-element value specified is lower than allowed, the minimum allowed number is used.</p>
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param usageType a type of usage that the palette is created for
     * @param elements elements that should be put to the data array
     * @return the indirect chunk palette
     * @since 1.0
     * @throws IllegalArgumentException if the bits-per-element value specified is higher than maximum allowed
     */
    /* TODO: | Replace the factory method with public constructor when flexible constructors get finally implemented
       TODO: | in Java */
    public static @NotNull IndirectChunkPalette create(byte bitsPerElement, @NotNull ChunkPaletteUsageType usageType,
                                                       int @NotNull [] elements) {
        NullabilityUtil.requireNonNull(elements, "elements");

        int maximumBitsPerElement = usageType.maximumIndirectBits();
        if (bitsPerElement > maximumBitsPerElement) {
            throw new IllegalArgumentException(String.format(
                    "The bits-per-element value specified is higher than maximum allowed (%d>%d)",
                    bitsPerElement, maximumBitsPerElement
            ));
        }

        bitsPerElement = MathUtil.max(usageType.minimumIndirectBits(), bitsPerElement);

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
                bitsPerElement, usageType,
                ChunkPalette.createDataArray(bitsPerElement, dataElements, usageType.axisLength()),
                elements, registryIndices
        );
    }
}