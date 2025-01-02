package net.hypejet.jet.server.world.chunk.palette;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.array.UnmodifiableLongArray;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a data container of {@linkplain net.hypejet.jet.server.world.chunk.section.ChunkSection a chunk section}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.section.ChunkSection
 */
public sealed abstract class ChunkPalette permits DirectChunkPalette, IndirectChunkPalette, SingleValuedChunkPalette {

    private final byte bitsPerElement;
    private final UnmodifiableLongArray data;

    /**
     * Constructs the {@linkplain ChunkPalette chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the should use
     * @param data the data as a long array, where one long can store multiple elements, number of elements that one
     *             long can have depends on the previous bits-per-element value
     * @since 1.0
     */
    protected ChunkPalette(byte bitsPerElement, long @NotNull [] data) {
        this.bitsPerElement = bitsPerElement;
        this.data = new UnmodifiableLongArray(NullabilityUtil.requireNonNull(data, "data"));
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
    public final long @NotNull [] data() {
        return this.data.array();
    }

    /**
     * Gets an element of the data at coordinates specified. Note that coordinate values provided on each axis must
     * be relative to beginning of the axles in the chunk palette.
     *
     * @param x an {@code X} value of the coordinates
     * @param y an {@code Y} value of the coordinates
     * @param z an {@code Z} value of the coordinates
     * @return the element
     * @since 1.0
     */
    public abstract int getElement(byte x, byte y, byte z);

    /**
     * Creates a long array containing elements specified, where one long can store multiple elements. Number of
     * elements that one long can have depends on the bits-per-element value specified.
     *
     * @param bitsPerElement the bits-per-element value
     * @param elements the elements
     * @param axisLength a length of each axis of chunk palette that the data array is created for
     * @return the long array
     * @since 1.0
     * @throws IllegalArgumentException if the length of elements specified is invalid or count of bits for one of
     *                                  elements specified is higher than bits-per-element value specified
     */
    protected static long @NotNull [] createDataArray(byte bitsPerElement, int @NotNull [] elements, byte axisLength) {
        int expectedElementLength = axisLength * axisLength * axisLength;
        int elementsLength = elements.length;

        if (expectedElementLength != elementsLength) {
            throw new IllegalArgumentException(String.format(
                    "The length of elements specified (%d) for an axis length specified (%d) must be %d",
                    elementsLength, axisLength, expectedElementLength
            ));
        }

        byte elementsPerLong = (byte) (Math.floorDiv(Long.SIZE, bitsPerElement));
        long[] values = new long[Math.ceilDiv(elements.length, elementsPerLong)];

        int valueIndex = 0;

        long value = 0L;
        byte bitShift = 0;

        for (int index = 0; index < elements.length; index++) {
            int element = elements[index];
            int elementBits = Integer.SIZE - Integer.numberOfLeadingZeros(element);

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
     * Calculates index of that an element with coordinates specified is stored at.
     *
     * @param axisLength a length of each axis of the coordinates
     * @param x an {@code X} value of the coordinates
     * @param y an {@code Y} value of the coordinates
     * @param z an {@code Z} value of the coordinates
     * @return the index
     * @since 1.0
     */
    protected static int calculateElementIndex(byte axisLength, byte x, byte y, byte z) {
        validateValue(axisLength, x, "x");
        validateValue(axisLength, y, "y");
        validateValue(axisLength, z, "z");
        return x + (axisLength * z) + (axisLength * axisLength * y);
    }

    /**
     * Validates whether a coordinate value specified for an axis length specified is correct.
     *
     * @param coordinateValue the coordinate value
     * @param axisLength the axis length
     * @param axisName a name of the axis
     * @since 1.0
     */
    private static void validateValue(byte coordinateValue, byte axisLength, @NotNull String axisName) {
        NullabilityUtil.requireNonNull(axisName, "axis name");
        if (coordinateValue >= axisLength) {
            throw new IllegalArgumentException(String.format(
                    "Coordinate value of axis \"%s\" is higher than or the same as length of the axis (%s>=%s)",
                    axisName, coordinateValue, axisLength
            ));
        }
    }
}