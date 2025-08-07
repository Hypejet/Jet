package net.hypejet.jet.server.world.chunk.factory.palette;

import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.SingleValuedChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.chunk.factory.palette.ChunkPaletteFactory;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents an implementation of {@linkplain ChunkPaletteFactory a chunk-palette factory}.
 *
 * @param <E> a type of elements of chunk palettes that the chunk palette factory creates
 * @since 1.0
 * @see ChunkPaletteFactory
 */
public final class JetChunkPaletteFactory<E> implements ChunkPaletteFactory<E> {

    private final ChunkPaletteType paletteType;
    private final AbstractChunkPalette.IndexSpecification<E> indexSpecification;

    /**
     * Constructs the {@linkplain JetChunkPaletteFactory chunk-palette factory implementation}.
     *
     * @param paletteType a type of chunk palettes that the chunk-palette factory should create
     * @param indexSpecification an index specification specifying registry indices for elements of palettes
     *                           that the constructed chunk-palette factory should create
     * @since 1.0
     */
    public JetChunkPaletteFactory(@NonNull ChunkPaletteType paletteType,
                                  AbstractChunkPalette.@NonNull IndexSpecification<E> indexSpecification) {
        this.paletteType = Objects.requireNonNull(paletteType, "palette type");
        this.indexSpecification = Objects.requireNonNull(indexSpecification, "index specification");
    }

    @Override
    public @NonNull AbstractChunkPalette<E> createSingleValued(@NonNull E element) {
        Objects.requireNonNull(element, "element");
        return new SingleValuedChunkPalette<>(this.paletteType, element, this.indexSpecification);
    }

    @Override
    public @NonNull AbstractChunkPalette<E> createDirect(@NonNull List<E> elements) {
        Objects.requireNonNull(elements, "elements");
        return AbstractChunkPalette.create(this.paletteType, this.indexSpecification, elements);
    }
}