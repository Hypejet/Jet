package net.hypejet.jet.server.world.coordinate.chunk.relative;

/**
 * Represents a position of {@linkplain net.hypejet.jet.data.model.api.registries.biome.Biome a biome}, which
 * is relative to a beginning of {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk}.
 *
 * @param relativeX a chunk-relative {@code X} value that the position should have
 * @param absoluteY an absolute {@code Y} value that the position should have
 * @param relativeZ a chunk-relative {@code Z} value that the position should have
 * @since 1.0
 * @see net.hypejet.jet.world.chunk.Chunk
 */
public record ChunkRelativeBiomePosition(byte relativeX, short absoluteY, byte relativeZ) {}