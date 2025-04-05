package net.hypejet.jet.world.chunk.factory.section;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.chunk.light.LightSection;
import net.hypejet.jet.world.chunk.light.LightStorage;
import net.hypejet.jet.world.chunk.section.ChunkPalette;
import net.hypejet.jet.world.chunk.section.ChunkSection;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a factory of {@linkplain ChunkSection chunk sections} and {@linkplain LightSection light sections}.
 *
 * @param <CS> a type of chunk sections that the factory creates
 * @param <LS> a type of light sections that the factory creates
 * @param <BS> a type of block states that block state palettes of chunk sections contain
 * @since 1.0
 * @see ChunkSection
 * @see LightSection
 */
public interface ChunkSectionFactory<CS extends ChunkSection<BS>, LS extends LightSection, BS> {
    /**
     * Creates {@linkplain ChunkSection a chunk section} with {@linkplain ChunkPalette chunk palettes} specified
     * of block states and biomes.
     *
     * @param blockStatePalette the chunk palette containing block states
     * @param biomePalette the chunk palette containing biomes
     * @return the chunk section
     * @since 1.0
     */
    @NonNull CS createChunkSection(@NonNull ChunkPalette<BS> blockStatePalette,
                                   @NonNull ChunkPalette<RegistryEntry<Biome>> biomePalette);

    /**
     * Creates {@linkplain LightSection a light section} with {@linkplain LightStorage light storages} specified
     * of skylight values and block light values.
     *
     * @param skyLightStorage the light storage containing skylight values
     * @param blockLightStorage the light storage containing block light values
     * @return the light section
     * @since 1.0
     */
    @NonNull LS createLightSection(@NonNull LightStorage skyLightStorage, @NonNull LightStorage blockLightStorage);
}