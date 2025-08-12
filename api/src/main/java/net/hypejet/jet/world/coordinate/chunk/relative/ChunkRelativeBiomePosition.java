package net.hypejet.jet.world.coordinate.chunk.relative;

import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.chunk.Chunk;

/**
 * A {@linkplain Chunk chunk}-relative position of a {@linkplain Biome biome}.
 *
 * @param relativeX a chunk-relative {@code X} axis value of the biome position
 * @param absoluteY an absolute {@code Y} axis value of the biome position
 * @param relativeZ a chunk-relative {@code Z} axis value of the biome position
 * @since 1.0
 * @see net.hypejet.jet.world.chunk.Chunk
 */
public record ChunkRelativeBiomePosition(byte relativeX, short absoluteY, byte relativeZ) {}