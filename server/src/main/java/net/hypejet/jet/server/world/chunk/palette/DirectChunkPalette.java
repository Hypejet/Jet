package net.hypejet.jet.server.world.chunk.palette;

import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortMaps;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenCustomHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.hash.IdentityHashStrategy;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
    private final Object2ShortMap<E> elementCountMap;

    /**
     * Constructs the {@linkplain DirectChunkPalette direct chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param type a type of which the palette should be
     * @param elements elements that should be put to the data array
     * @param elementOrder an element order, from which identifiers of elements of the palette should be retrieved
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @since 1.0
     */
    private DirectChunkPalette(byte bitsPerElement, @NonNull ChunkPaletteType type, @NonNull List<E> elements,
                               @NonNull ElementOrder<E> elementOrder,
                               @NonNull Object2ShortMap<E> elementCountMap) {
        super(bitsPerElement, type, createDataArray(
                bitsPerElement,
                type,
                NullabilityUtil.requireNonNull(elements, "elements"),
                NullabilityUtil.requireNonNull(elementOrder, "element order")
        ), elementOrder);

        this.elements = List.copyOf(elements);
        this.elementCountMap = Object2ShortMaps.unmodifiable(
                new Object2ShortOpenCustomHashMap<>(
                        NullabilityUtil.requireNonNull(elementCountMap, "element count map"),
                        IdentityHashStrategy.INSTANCE
                )
        );
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

        int elementIndex = ChunkPalette.calculateElementIndex(position);

        E element = this.elements.get(elementIndex);
        if (element == null)
            throw new IllegalArgumentException(String.format("Invalid element index: %d", elementIndex));

        return element;
    }

    @Override
    public @NonNull Object2ShortMap<E> elementCountMap() {
        return this.elementCountMap;
    }

    @Override
    protected @NonNull List<E> createElementList() {
        return this.elements;
    }

    /**
     * Creates {@linkplain DirectChunkPalette a direct chunk palette}.
     *
     * @param type a type of which the palette should be
     * @param elements elements that should be put to the data array
     * @param elementOrder an element order, from which identifiers of elements of the palette should be retrieved
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @return the direct chunk palette
     * @since 1.0
     */
    /* TODO: | Replace the factory method with public constructor when flexible constructors get finally implemented
       TODO: | in Java */
    static <E> @NonNull DirectChunkPalette<E> create(@NonNull ChunkPaletteType type, @NonNull List<E> elements,
                                                     @NonNull ElementOrder<E> elementOrder,
                                                     @NonNull Object2ShortMap<E> elementCountMap) {
        byte bitsPerElement = type.minimumDirectBits();

        for (E element : elementCountMap.keySet()) {
            int elementIdentifier = elementOrder.identifierOf(element);
            if (elementIdentifier == 0) continue;
            bitsPerElement = (byte) Math.max(MathUtil.bitCount(elementIdentifier), bitsPerElement);
        }

        return new DirectChunkPalette<>(bitsPerElement, type, elements, elementOrder, elementCountMap);
    }

    // TODO: Move this to the constructor when flexible constructors get finally implemented in Java
    private static <E> long @NonNull [] createDataArray(byte bitsPerElement, @NonNull ChunkPaletteType type,
                                                        @NotNull List<E> elements,
                                                        @NonNull ElementOrder<E> elementOrder) {
        if (bitsPerElement > MAXIMUM_BITS_PER_ELEMENT) {
            throw new IllegalArgumentException(String.format(
                    "The bits-per-element value specified exceeds the maximum value allowed (%d>%d)",
                    bitsPerElement, MAXIMUM_BITS_PER_ELEMENT
            ));
        }

        bitsPerElement = (byte) Math.max(type.minimumDirectBits(), bitsPerElement);

        int[] elementsAsIntegers = new int[elements.size()];
        for (int index = 0; index < elementsAsIntegers.length; index++)
            elementsAsIntegers[index] = elementOrder.identifierOf(elements.get(index));

        return ChunkPalette.createDataArray(bitsPerElement, elementsAsIntegers, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
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