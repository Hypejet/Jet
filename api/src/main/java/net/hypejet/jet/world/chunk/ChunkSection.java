package net.hypejet.jet.world.chunk;

import net.hypejet.jet.world.chunk.palette.Palette;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a 16x16x16 section of a {@linkplain Chunk chunk}.
 *
 * @param blockCount a count of non-air blocks in the section
 * @param chunkData a palette, which consists of 4096 entries, representing all the blocks in the chunk section
 * @param biomeData a palette, which consists of 64 entries, representing 4×4×4 biome regions in the chunk section
 * @since 1.0
 * @author Codestech
 * @see Chunk
 */
public record ChunkSection(short blockCount, @NonNull Palette chunkData, @NonNull Palette biomeData) {}