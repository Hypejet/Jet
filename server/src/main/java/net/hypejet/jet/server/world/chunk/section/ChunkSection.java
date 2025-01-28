package net.hypejet.jet.server.world.chunk.section;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a part of {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk}.
 *
 * @param blockCount a count of non-air block states of the chunk section
 * @param blockStatePalette a chunk palette of block states of the chunk section
 * @param biomePalette a chunk palette of biomes of the chunk section
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 */
public record ChunkSection(short blockCount, @NotNull ChunkPalette<JetBlockState> blockStatePalette,
                           @NotNull ChunkPalette<JetRegistryEntry<Biome>> biomePalette) {
    /**
     * Constructs the {@linkplain ChunkSection chunk section}.
     *
     * @param blockCount a count of non-air block states of the chunk section
     * @param blockStatePalette a chunk palette of block states of the chunk section
     * @param biomePalette a chunk palette of biomes of the chunk section
     * @since 1.0
     * @throws IllegalArgumentException if usage types of palettes specified are invalid
     */
    public ChunkSection {
        NullabilityUtil.requireNonNull(blockStatePalette, "block state palette");
        NullabilityUtil.requireNonNull(biomePalette, "biome palette");

        if (blockStatePalette.type() != ChunkPaletteType.BLOCK_STATE)
            throw new IllegalArgumentException("Type of the block state palette is not a block state palette type");
        if (biomePalette.type() != ChunkPaletteType.BIOME)
            throw new IllegalArgumentException("Type of the biome palette is not a biome palette type");
    }
}