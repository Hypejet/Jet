package net.hypejet.jet.world.chunk;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.block.BlockState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a builder of a chunk of {@linkplain net.hypejet.jet.world.World a world}.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.World
 */
public interface ChunkBuilder {
    /**
     * Sets {@linkplain BlockState a block state} at a chunk-relative block coordinate with values specified.
     *
     * @param x a chunk-relative {@code X} value of the coordinate
     * @param y an absolute {@code Y} value of the coordinate
     * @param z a chunk-relative {@code Z} value of the coordinate
     * @param blockState a block state that should be set at the coordinate specified
     * @see 1.0
     */
    void setBlockState(byte x, short y, byte z, @NonNull BlockState blockState);

    /**
     * Sets {@linkplain Biome a biome} at a chunk-relative biome coordinate with values specified.
     *
     * @param x a chunk-relative {@code X} value of the coordinate
     * @param y an absolute {@code Y} value of the coordinate
     * @param z a chunk-relative {@code Z} value of the coordinate
     * @param biome a registry entry of a biome that should be set at the coordinate specified
     * @see 1.0
     */
    void setBiome(byte x, short y, byte z, @NonNull RegistryEntry<Biome> biome);

    /**
     * Sets a skylight level at a chunk-relative block coordinate with values specified.
     *
     * @param x a chunk-relative {@code X} value of the coordinate
     * @param y an absolute {@code Y} value of the coordinate
     * @param z a chunk-relative {@code Z} value of the coordinate
     * @param value a value of the skylight level that should be set at the coordinate specified
     * @since 1.0
     */
    void setSkyLight(byte x, short y, byte z, byte value);

    /**
     * Sets a block light level at a chunk-relative block coordinate with values specified.
     *
     * @param x a chunk-relative {@code X} value of the coordinate
     * @param y an absolute {@code Y} value of the coordinate
     * @param z a chunk-relative {@code Z} value of the coordinate
     * @param value a value of the block light level that should be set at the coordinate specified
     * @since 1.0
     */
    void setBlockLight(byte x, short y, byte z, byte value);
}