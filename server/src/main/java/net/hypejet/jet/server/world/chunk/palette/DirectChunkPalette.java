package net.hypejet.jet.server.world.chunk.palette;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.function.IntResultingFunction;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents {@linkplain ChunkPalette a chunk palette}, which stores all elements directly in the data array.
 *
 * @since 1.0
 * @param <E> a type of elements that the palette stores
 * @see ChunkPalette
 */
public final class DirectChunkPalette<E> extends ChunkPalette<E> {

    private static final byte MAXIMUM_BITS_PER_ELEMENT = Integer.SIZE - 1;

    private final List<E> elements;
    private final Map<E, Integer> elementCountMap;

    /**
     * Constructs the {@linkplain DirectChunkPalette direct chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param type a type of which the palette should be
     * @param elements elements that should be put to the data array
     * @param elementToIdentifierFunction a function, which should represent elements of the palette as integers
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @since 1.0
     */
    private DirectChunkPalette(byte bitsPerElement, @NonNull ChunkPaletteType type, @NonNull List<E> elements,
                               @NonNull IntResultingFunction<E> elementToIdentifierFunction,
                               @NonNull Map<E, Integer> elementCountMap) {
        super(bitsPerElement, type, createDataArray(
                bitsPerElement,
                type,
                NullabilityUtil.requireNonNull(elements, "elements"),
                NullabilityUtil.requireNonNull(elementToIdentifierFunction, "element to identifier function")
        ), elementToIdentifierFunction);

        this.elements = List.copyOf(elements);
        this.elementCountMap = Map.copyOf(elementCountMap);
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
     * Creates {@linkplain DirectChunkPalette a direct chunk palette}.
     *
     * @param type a type of which the palette should be
     * @param elements elements that should be put to the data array
     * @param elementToIdentifierFunction a function, which should represent elements of the palette as integers
     * @return the direct chunk palette
     * @since 1.0
     */
    /* TODO: | Replace the factory method with public constructor when flexible constructors get finally implemented
       TODO: | in Java */
    public static <E> @NonNull DirectChunkPalette<E> create(
            @NonNull ChunkPaletteType type, @NonNull List<E> elements,
            @NonNull IntResultingFunction<E> elementToIdentifierFunction
    ) {
        return create(type, elements, elementToIdentifierFunction, ChunkPalette.createCountMap(elements));
    }

    /**
     * Creates {@linkplain DirectChunkPalette a direct chunk palette}.
     *
     * @param type a type of which the palette should be
     * @param elements elements that should be put to the data array
     * @param elementToIdentifierFunction a function, which should represent elements of the palette as integers
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @return the direct chunk palette
     * @since 1.0
     */
    /* TODO: | Replace the factory method with public constructor when flexible constructors get finally implemented
       TODO: | in Java */
    static <E> @NonNull DirectChunkPalette<E> create(@NonNull ChunkPaletteType type, @NonNull List<E> elements,
                                                     @NonNull IntResultingFunction<E> elementToIdentifierFunction,
                                                     @NonNull Map<E, Integer> elementCountMap) {
        byte bitsPerElement = type.minimumDirectBits();

        for (E element : elementCountMap.keySet()) {
            int elementIdentifier = elementToIdentifierFunction.apply(element);
            if (elementIdentifier == 0) continue;
            bitsPerElement = (byte) Math.max(MathUtil.bitCount(elementIdentifier), bitsPerElement);
        }

        return new DirectChunkPalette<>(bitsPerElement, type, elements, elementToIdentifierFunction, elementCountMap);
    }

    // TODO: Move this to the constructor when flexible constructors get finally implemented in Java
    private static <E> long @NonNull [] createDataArray(byte bitsPerElement, @NonNull ChunkPaletteType type,
                                                        @NotNull List<E> elements,
                                                        @NonNull IntResultingFunction<E> elementToIdentifierFunction) {
        if (bitsPerElement > MAXIMUM_BITS_PER_ELEMENT) {
            throw new IllegalArgumentException(String.format(
                    "The bits-per-element value specified exceeds the maximum value allowed (%d>%d)",
                    bitsPerElement, MAXIMUM_BITS_PER_ELEMENT
            ));
        }

        bitsPerElement = (byte) Math.max(type.minimumDirectBits(), bitsPerElement);

        int[] elementsAsIntegers = new int[elements.size()];
        for (int index = 0; index < elementsAsIntegers.length; index++)
            elementsAsIntegers[index] = elementToIdentifierFunction.apply(elements.get(index));

        return ChunkPalette.createDataArray(bitsPerElement, elementsAsIntegers, type);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DirectChunkPalette<?> otherPalette)) return false;
        return Objects.equals(this.elements, otherPalette.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.elements);
    }

    @Override
    public String toString() {
        return "DirectChunkPalette{" +
                "elements=" + this.elements +
                '}';
    }
}