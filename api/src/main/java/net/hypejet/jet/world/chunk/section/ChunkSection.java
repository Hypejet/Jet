package net.hypejet.jet.world.chunk.section;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents section of block and biome data of {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk}.
 *
 * @param <BS> a type of block states that block state palette of the chunk section contain
 * @since 1.0
 * @see net.hypejet.jet.world.chunk.Chunk
 */
public interface ChunkSection<BS> {
    /**
     * Gets {@linkplain ChunkPalette a chunk palette}, which stores block states of this chunk section.
     *
     * @return the chunk palette
     * @since 1.0
     */
    @NonNull ChunkPalette<BS> blockStatePalette();

    /**
     * Gets {@linkplain ChunkPalette a chunk palette}, which stores biomes of this chunk section.
     *
     * @return the chunk palette
     * @since 1.0
     */
    @NonNull ChunkPalette<RegistryEntry<Biome>> biomePalette();
}