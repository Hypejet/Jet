package net.hypejet.jet.server.world.chunk.section;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
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
     */
    public ChunkSection {
        NullabilityUtil.requireNonNull(blockPalette, "block palette");
        NullabilityUtil.requireNonNull(biomePalette, "biome palette");
    }
}