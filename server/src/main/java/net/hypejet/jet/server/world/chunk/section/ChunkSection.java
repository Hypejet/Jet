package net.hypejet.jet.server.world.chunk.section;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.usage.ChunkPaletteUsageType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a part of {@linkplain net.hypejet.jet.server.world.chunk.Chunk a chunk}.
 *
 * @param blockCount a count of non-air block states of the chunk section
 * @param blockPalette a chunk palette of block states of the chunk section
 * @param biomePalette a chunk palette of biomes of the chunk section
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 */
public record ChunkSection(short blockCount, @NotNull ChunkPalette blockPalette, @NotNull ChunkPalette biomePalette) {
    /**
     * Constructs the {@linkplain ChunkSection chunk section}.
     *
     * @param blockCount a count of non-air block states of the chunk section
     * @param blockPalette a chunk palette of block states of the chunk section
     * @param biomePalette a chunk palette of biomes of the chunk section
     * @since 1.0
     * @throws IllegalArgumentException if usage types of palettes specified are invalid
     */
    public ChunkSection {
        NullabilityUtil.requireNonNull(blockPalette, "block palette");
        NullabilityUtil.requireNonNull(biomePalette, "biome palette");

        if (blockPalette.usageType() != ChunkPaletteUsageType.BLOCK)
            throw new IllegalArgumentException("Usage type of the block palette is not a block usage type");
        if (biomePalette.usageType() != ChunkPaletteUsageType.BIOME)
            throw new IllegalArgumentException("Usage type of the biome palette is not a biome usage type");
    }
}