package net.hypejet.jet.world.chunk.section;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockState;
import org.jspecify.annotations.NonNull;

/**
 * Represents section of block and biome data of {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk}.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.chunk.Chunk
 */
public interface ChunkSection {
    /**
     * Gets {@linkplain ChunkPalette a chunk palette}, which stores block states of this chunk section.
     *
     * @return the chunk palette
     * @since 1.0
     */
    @NonNull ChunkPalette<BlockState> blockStatePalette();

    /**
     * Gets {@linkplain ChunkPalette a chunk palette}, which stores biomes of this chunk section.
     *
     * @return the chunk palette
     * @since 1.0
     */
    @NonNull ChunkPalette<Holder.Reference<Biome>> biomePalette();
}