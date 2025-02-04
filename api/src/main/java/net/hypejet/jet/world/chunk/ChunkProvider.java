package net.hypejet.jet.world.chunk;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.block.BlockState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that creates chunks for {@linkplain World a world}.
 *
 * @since 1.0
 */
public interface ChunkProvider {
    /**
     * Creates a chunk.
     *
     * @param builder a builder of the chunk
     * @param chunkX an {@code X} value of coordinate of the chunk
     * @param chunkZ an {@code Z} value of coordinate of the chunk
     * @param world a world that the chunk is created for
     * @since 1.0
     */
    void provide(@NonNull ChunkBuilder builder, int chunkX, int chunkZ, @NonNull World world);

    /**
     * Gets {@linkplain BlockState a block state} that should be used in places where block states have not been
     * set while providing a chunk with coordinates specified.
     *
     * @param chunkX an {@code X} value of coordinate of the chunk
     * @param chunkZ an {@code Z} value of coordinate of the chunk
     * @return the block state
     * @since 1.0
     */
    @NonNull BlockState defaultBlockState(int chunkX, int chunkZ);

    /**
     * Gets {@linkplain RegistryEntry a registry entry} of {@linkplain Biome a biome} that should be used in places
     * where biomes have not been set while providing a chunk with coordinates specified.
     *
     * @param chunkX an {@code X} value of coordinate of the chunk
     * @param chunkZ an {@code Z} value of coordinate of the chunk
     * @return the registry entry
     * @since 1.0
     */
    @NonNull RegistryEntry<Biome> defaultBiome(int chunkX, int chunkZ);
}