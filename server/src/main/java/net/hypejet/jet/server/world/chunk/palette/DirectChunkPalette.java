package net.hypejet.jet.server.world.chunk.palette;

import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortMaps;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.util.storage.BitStorage;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain AbstractChunkPalette a chunk palette}, which stores all elements directly in the data array.
 *
 * @since 1.0
 * @param <E> a type of elements that the palette stores
 * @see AbstractChunkPalette
 */
public final class DirectChunkPalette<E> extends AbstractChunkPalette<E> {

    private final BitStorage storage;
    private final Object2ShortMap<E> elementCountMap;

    /**
     * Constructs the {@linkplain DirectChunkPalette direct chunk palette}.
     *
     * @param bitsPerElement number of bits that each element of the data should use
     * @param type a type of which the palette should be
     * @param storage a bit storage that stores registry indices of elements that the chunk palette should contain
     * @param indexSpecification an index specification specifying registry indices
     *                           for elements of the constructed palette
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @since 1.0
     */
    private DirectChunkPalette(byte bitsPerElement, @NonNull ChunkPaletteType type,
                               @NonNull BitStorage storage, @NonNull IndexSpecification<E> indexSpecification,
                               @NonNull Object2ShortMap<E> elementCountMap) {
        super(bitsPerElement, type, Objects.requireNonNull(storage, "storage").data(), indexSpecification);
        Objects.requireNonNull(elementCountMap, "element count map");
        this.storage = storage;
        this.elementCountMap = Object2ShortMaps.unmodifiable(new Object2ShortOpenHashMap<>(elementCountMap));
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

        int elementIndex = AbstractChunkPalette.calculateElementIndex(position);
        int elementRegistryIndex = this.storage.getElement(elementIndex);
        return this.indexSpecification().valueByIndex(elementRegistryIndex);
    }

    @Override
    public @NonNull Object2ShortMap<E> elementCountMap() {
        return this.elementCountMap;
    }

    @Override
    protected int @NonNull [] createElementArray() {
        return this.storage.unpack();
    }

    /**
     * Creates {@linkplain DirectChunkPalette a direct chunk palette}.
     *
     * @param type a type of which the palette should be
     * @param elements elements that the chunk palette should have
     * @param indexSpecification an index specification specifying registry indices for elements of the created palette
     * @param elementCountMap a map, which maps elements to their count in the palette
     * @return the direct chunk palette
     * @since 1.0
     */
    /* TODO: | Replace the factory method with public constructor when flexible constructors get finally implemented
       TODO: | in Java */
    static <E> @NonNull DirectChunkPalette<E> create(@NonNull ChunkPaletteType type, int @NonNull [] elements,
                                                     @NonNull IndexSpecification<E> indexSpecification,
                                                     @NonNull Object2ShortMap<E> elementCountMap) {
        byte bitsPerElement = type.minimumDirectBits();
        for (E element : elementCountMap.keySet()) {
            int elementRegistryIndex = indexSpecification.indexFor(element);
            bitsPerElement = (byte) Math.max(MathUtil.bitCount(elementRegistryIndex), bitsPerElement);
        }

        BitStorage storage = new BitStorage(bitsPerElement, elements);
        return new DirectChunkPalette<>(bitsPerElement, type, storage, indexSpecification, elementCountMap);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DirectChunkPalette<?> && super.equals(o);
    }
}