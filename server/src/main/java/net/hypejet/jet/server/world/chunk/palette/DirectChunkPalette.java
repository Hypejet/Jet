package net.hypejet.jet.server.world.chunk.palette;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import net.hypejet.jet.server.world.chunk.palette.usage.ChunkPaletteUsageType;
import net.hypejet.jet.util.array.UnmodifiableIntegerArray;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@linkplain ChunkPalette a chunk palette}, which stores all elements directly in the data array.
 *
 * @since 1.0
 * @see ChunkPalette
 */
public final class DirectChunkPalette extends ChunkPalette {

    private static final byte MAXIMUM_BITS_PER_ELEMENT = 31;

    private final UnmodifiableIntegerArray elements;

    /**
     * Constructs the {@linkplain DirectChunkPalette direct chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param usageType a type of usage that the palette is created for
     * @param elements elements that should be put to the data array
     * @since 1.0
     */
    private DirectChunkPalette(byte bitsPerElement, @NotNull ChunkPaletteUsageType usageType,
                               int @NotNull [] elements) {
        super(bitsPerElement, usageType, ChunkPalette.createDataArray(
                bitsPerElement,
                NullabilityUtil.requireNonNull(elements, "elements"),
                usageType.axisLength()
        ));
        this.elements = new UnmodifiableIntegerArray(elements);
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
        return new DirectChunkPalette(bitsPerElement, usageType, elements);
    }

    /**
     * Creates the {@linkplain DirectChunkPalette direct chunk palette}.
     *
     * <p>If the bits-per-element value specified is lower than allowed, the minimum allowed number is used.</p>
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param usageType a type of usage that the palette is created for
     * @param elements elements that should be put to the data array
     * @return the direct chunk palette created
     * @since 1.0
     * @throws IllegalArgumentException if the bits-per-element value specified is higher than maximum allowed
     */
    /* TODO: | Replace the factory method with public constructor when flexible constructors get finally implemented
       TODO: | in Java */
    public static @NotNull DirectChunkPalette create(byte bitsPerElement, @NotNull ChunkPaletteUsageType usageType,
                                                     int @NotNull [] elements) {
        if (bitsPerElement > MAXIMUM_BITS_PER_ELEMENT) {
            throw new IllegalArgumentException(String.format(
                    "The bits-per-element value specified is higher than maximum allowed (%d>%d)",
                    bitsPerElement, MAXIMUM_BITS_PER_ELEMENT
            ));
        }

        bitsPerElement = MathUtil.max(usageType.minimumDirectBits(), bitsPerElement);
        return new DirectChunkPalette(bitsPerElement, usageType, elements);
    }
}