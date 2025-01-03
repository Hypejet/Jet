package net.hypejet.jet.server.world.chunk.palette;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.util.array.UnmodifiableIntegerArray;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@linkplain ChunkPalette a chunk palette}, which stores all elements directly in the data array.
 *
 * @since 1.0
 * @see ChunkPalette
 */
public final class DirectChunkPalette extends ChunkPalette {

    private final byte axisLength;
    private final UnmodifiableIntegerArray elements;

    /**
     * Constructs the {@linkplain DirectChunkPalette direct chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param elements elements that should be put to the data array
     * @param axisLength length that each axis of the chunk palette should have
     * @since 1.0
     */
    public DirectChunkPalette(byte bitsPerElement, int @NotNull [] elements, byte axisLength) {
        super(bitsPerElement, ChunkPalette.createDataArray(
                bitsPerElement, NullabilityUtil.requireNonNull(elements, "elements"), axisLength
        ));

        this.axisLength = axisLength;
        this.elements = new UnmodifiableIntegerArray(elements);
    }

    @Override
    public int getElement(byte x, byte y, byte z) {
        return this.elements.array()[ChunkPalette.calculateElementIndex(this.axisLength, x, y, z)];
    }
}