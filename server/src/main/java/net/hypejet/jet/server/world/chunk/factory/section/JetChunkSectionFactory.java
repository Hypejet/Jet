package net.hypejet.jet.server.world.chunk.factory.section;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.chunk.factory.section.ChunkSectionFactory;
import net.hypejet.jet.world.chunk.light.LightStorage;
import net.hypejet.jet.world.chunk.section.ChunkPalette;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@linkplain ChunkSectionFactory a chunk section factory}.
 *
 * @since 1.0
 * @see ChunkSectionFactory
 */
public final class JetChunkSectionFactory implements ChunkSectionFactory {
    /**
     * An instance of the {@linkplain JetChunkSectionFactory chunk-section factory}.
     *
     * @since 1.0
     */
    public static final JetChunkSectionFactory INSTANCE = new JetChunkSectionFactory();

    private JetChunkSectionFactory() {}

    @Override
    public @NonNull JetChunkSection createChunkSection(@NonNull ChunkPalette<BlockState> blockStatePalette,
                                                       @NonNull ChunkPalette<RegistryEntry<Biome>> biomePalette) {
        if (!(blockStatePalette instanceof AbstractChunkPalette<BlockState> validatedBlockStatePalette))
            throw new IllegalArgumentException("The block state palette specified is not a valid chunk palette");
        if (!(biomePalette instanceof AbstractChunkPalette<RegistryEntry<Biome>> validatedBiomePalette))
            throw new IllegalArgumentException("The biome palette specified is not a valid chunk palette");
        return new JetChunkSection(validatedBlockStatePalette, validatedBiomePalette);
    }

    @Override
    public @NonNull JetLightSection createLightSection(@NonNull LightStorage skyLightStorage,
                                                       @NonNull LightStorage blockLightStorage) {
        if (!(skyLightStorage instanceof AbstractLightStorage validatedSkyLightStorage))
            throw new IllegalArgumentException("The skylight storage specified is not a valid light storage");
        if (!(blockLightStorage instanceof AbstractLightStorage validatedBlockLightStorage))
            throw new IllegalArgumentException("The block light storage specified is not a valid light storage");
        return new JetLightSection(validatedSkyLightStorage, validatedBlockLightStorage);
    }
}