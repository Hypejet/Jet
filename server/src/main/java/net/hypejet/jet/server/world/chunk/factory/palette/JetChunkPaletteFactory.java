package net.hypejet.jet.server.world.chunk.factory.palette;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.order.ElementOrder;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.SingleValuedChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.chunk.factory.palette.ChunkPaletteFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents an implementation of {@linkplain ChunkPaletteFactory a chunk-palette factory}.
 *
 * @param <E> a type of elements of chunk palettes that the chunk palette factory creates
 * @since 1.0
 * @see ChunkPaletteFactory
 */
public final class JetChunkPaletteFactory<E> implements ChunkPaletteFactory<E> {

    private final ChunkPaletteType paletteType;
    private final ElementOrder<? extends E> elementOrder;

    /**
     * Constructs the {@linkplain JetChunkPaletteFactory chunk-palette factory implementation}.
     *
     * @param paletteType a type of chunk palettes that the chunk-palette factory should create
     * @param elementOrder an element order of all elements that should be able to be used
     *                     in the chunk-palettes created
     * @since 1.0
     */
    public JetChunkPaletteFactory(@NonNull ChunkPaletteType paletteType,
                                  @NonNull ElementOrder<? extends E> elementOrder) {
        this.paletteType = NullabilityUtil.requireNonNull(paletteType, "palette type");
        this.elementOrder = NullabilityUtil.requireNonNull(elementOrder, "element order");
    }

    @Override
    public @NonNull AbstractChunkPalette<E> createSingleValued(@NonNull E element) {
        NullabilityUtil.requireNonNull(element, "element");
        return new SingleValuedChunkPalette<>(this.paletteType, element, this.elementOrder);
    }

    @Override
    public @NonNull AbstractChunkPalette<E> createDirect(@NonNull List<E> elements) {
        NullabilityUtil.requireNonNull(elements, "elements");
        return AbstractChunkPalette.create(this.paletteType, this.elementOrder, elements);
    }
}