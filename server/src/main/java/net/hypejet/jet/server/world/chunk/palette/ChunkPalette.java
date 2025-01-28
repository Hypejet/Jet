package net.hypejet.jet.server.world.chunk.palette;

import com.google.common.collect.Iterables;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.array.UnmodifiableLongArray;
import net.hypejet.jet.server.util.function.IntResultingFunction;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.chunk.palette.update.ChunkPaletteUpdate;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Represents a data container of {@linkplain net.hypejet.jet.server.world.chunk.section.ChunkSection a chunk section}.
 *
 * @since 1.0
 * @param <E> a type of elements that the palette stores
 * @see net.hypejet.jet.server.world.chunk.section.ChunkSection
 */
public sealed abstract class ChunkPalette<E> permits DirectChunkPalette, IndirectChunkPalette,
        SingleValuedChunkPalette {

    private static final BiFunction<Object, ? super Integer, ? extends Integer>
            COUNT_MAP_INCREMENT_FUNCTION = (ignored, integer) -> integer == null ? 1 : integer + 1;

    private final byte bitsPerElement;
    private final UnmodifiableLongArray data;

    private final ChunkPaletteType type;
    private final IntResultingFunction<E> elementToIdentifierFunction;

    /**
     * Constructs the {@linkplain ChunkPalette chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the should use
     * @param type a type of which the palette should be
     * @param data the data as a long array, where one long can store multiple elements, number of elements that one
     *             long can have depends on the previous bits-per-element value
     * @param elementToIdentifierFunction a function, which should represent elements of the palette as integers
     * @since 1.0
     */
    protected ChunkPalette(byte bitsPerElement, @NonNull ChunkPaletteType type, long @NonNull [] data,
                           @NonNull IntResultingFunction<E> elementToIdentifierFunction) {
        this.bitsPerElement = bitsPerElement;
        this.type = NullabilityUtil.requireNonNull(type, "type");
        this.data = new UnmodifiableLongArray(NullabilityUtil.requireNonNull(data, "data"));
        this.elementToIdentifierFunction = NullabilityUtil.requireNonNull(
                elementToIdentifierFunction,
                "element to identifier function"
        );
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
     * Gets a function represents elements of this palette as integers.
     *
     * @return the function
     * @since 1.0
     */
    public final @NonNull IntResultingFunction<E> elementToIdentifierFunction() {
        return this.elementToIdentifierFunction;
    }

    /**
     * Creates a new {@linkplain ChunkPalette chunk palette} with {@linkplain ChunkPaletteUpdate chunk palette updates}
     * specified.
     *
     * @param updates the updates
     * @return the new chunk palette
     * @since 1.0
     */
    @SafeVarargs
    public final @NonNull ChunkPalette<E> withUpdates(@NonNull ChunkPaletteUpdate<E> @NonNull ... updates) {
        if (updates.length == 0)
            return this;

        ChunkPaletteType type = this.type();

        List<E> elements = new ArrayList<>(this.createElementList());
        Map<E, Integer> elementCountMap = new IdentityHashMap<>(this.elementCountMap());

        boolean paletteChanged = false;
        for (ChunkPaletteUpdate<E> update : updates) {
            int elementIndex = calculateElementIndex(
                    type.axisLength(), update.sectionX(), update.sectionY(), update.sectionZ()
            );

            E previousElement = elements.get(elementIndex);
            E newElement = update.newElement();

            if (previousElement == newElement) continue;
            if (!paletteChanged) paletteChanged = true;

            elements.set(elementIndex, newElement);
            elementCountMap.compute(newElement, COUNT_MAP_INCREMENT_FUNCTION);

            elementCountMap.compute(previousElement, (ignored, integer) -> {
                if (integer == null)
                    return null;

                int decreasedValue = integer - 1;
                if (decreasedValue <= 0)
                    return null;

                return decreasedValue;
            });
        }

        if (!paletteChanged)
            return this;

        IntResultingFunction<E> elementToIdentifierFunction = this.elementToIdentifierFunction();
        if (elementCountMap.size() == 1) {
            E onlyElement = Iterables.getOnlyElement(elementCountMap.keySet());
            return new SingleValuedChunkPalette<>(type, onlyElement, elementToIdentifierFunction);
        }

        IndirectChunkPalette<E> indirectPalette = IndirectChunkPalette.createOrNull(
                type, elements, elementCountMap,
                elementToIdentifierFunction
        );

        if (indirectPalette != null)
            return indirectPalette;
        return DirectChunkPalette.create(type, elements, elementToIdentifierFunction, elementCountMap);
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
    public abstract @NonNull E getElement(byte x, byte y, byte z);

    /**
     * Gets {@linkplain Map a map}, which maps elements to their count in the palette.
     *
     * @return the map
     * @since 1.0
     */
    public abstract @NonNull Map<E, Integer> elementCountMap();

    /**
     * Creates {@linkplain List a list} of elements of this palette.
     *
     * @return the list
     * @since 1.0
     */
    protected abstract @NonNull List<E> createElementList();

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
    public static int calculateElementIndex(byte axisLength, byte x, byte y, byte z) {
        validateValue(x, axisLength, "x");
        validateValue(y, axisLength, "y");
        validateValue(z, axisLength, "z");
        return x + (axisLength * z) + (axisLength * axisLength * y);
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
     * Creates {@linkplain Map a map}, which maps elements to their count in the element list specified.
     *
     * @param elements the element list
     * @return the map
     * @param <E> a type of elements of the element list
     * @since 1.0
     */
    protected static <E> @NonNull Map<E, Integer> createCountMap(@NonNull List<E> elements) {
        Map<E, Integer> elementCountMap = new IdentityHashMap<>();
        for (E element : elements)
            elementCountMap.compute(element, COUNT_MAP_INCREMENT_FUNCTION);
        return elementCountMap;
    }

    /**
     * Validates whether a coordinate value specified for an axis length specified is correct.
     *
     * @param coordinateValue the coordinate value
     * @param axisLength the axis length
     * @param axisName a name of the axis
     * @since 1.0
     */
    private static void validateValue(byte coordinateValue, byte axisLength, @NonNull String axisName) {
        NullabilityUtil.requireNonNull(axisName, "axis name");
        if (coordinateValue >= axisLength) {
            throw new IllegalArgumentException(String.format(
                    "Coordinate value of axis \"%s\" is higher than or the same as length of the axis (%s>=%s)",
                    axisName, coordinateValue, axisLength
            ));
        }
    }
}