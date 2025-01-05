package net.hypejet.jet.server.world.chunk.palette;

import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.server.world.chunk.palette.usage.ChunkPaletteUsageType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Represents {@linkplain ChunkPalette a chunk palette}, which contains the same element at each position.
 *
 * @since 1.0
 * @see ChunkPalette
 */
public final class SingleValuedChunkPalette extends ChunkPalette {

    private static final long[] EMPTY_LONG_ARRAY = new long[0];

    private final int element;

    /**
     * Constructs the {@linkplain SingleValuedChunkPalette single-valued chunk palette}.
     *
     * @param usageType a type of usage that the palette is created for
     * @param element the element
     * @since 1.0
     */
    public SingleValuedChunkPalette(@NotNull ChunkPaletteUsageType usageType, int element) {
        super((byte) 0, usageType, EMPTY_LONG_ARRAY);
        this.element = element;
    }

    @Override
    public int getElement(byte x, byte y, byte z) {
        return this.element;
    }

    @Override
    public @NotNull ChunkPalette withUpdates(@NotNull ChunkPaletteUpdate @NotNull ... updates) {
        if (updates.length == 0)
            return this;

        // TODO: Do the same check with direct and indirect?
        boolean updateNeeded = false;
        for (ChunkPaletteUpdate update : updates) {
            if (update.newElement() != this.element) {
                updateNeeded = true;
                break;
            }
        }

        if (!updateNeeded)
            return this;

        ChunkPaletteUsageType usageType = this.usageType();
        byte axisLength = usageType.axisLength();

        int[] elements = new int[axisLength * axisLength * axisLength];
        Arrays.fill(elements, this.element);

        byte bitsPerElement = this.bitsPerElement();
        for (ChunkPaletteUpdate update : updates) {
            int elementIndex = ChunkPalette.calculateElementIndex(
                    update.sectionX(), update.sectionY(), update.sectionZ(), axisLength
            );

            int newElement = update.newElement();
            elements[elementIndex] = newElement;
            bitsPerElement = MathUtil.max(bitsPerElement, (byte) MathUtil.ceilLog2(newElement));
        }

        byte indirectBitsPerElement = (byte) MathUtil.ceilLog2(elements.length);
        if (MathUtil.isInRange(indirectBitsPerElement,
                usageType.minimumIndirectBits(),
                usageType.maximumIndirectBits())) {
            return IndirectChunkPalette.create(indirectBitsPerElement, usageType, elements);
        }

        return DirectChunkPalette.create(bitsPerElement, usageType, elements);
    }

    /**
     * Gets the element that this chunk palette returns for each position.
     *
     * @return the element
     * @since 1.0
     */
    public int element() {
        return this.element;
    }
}