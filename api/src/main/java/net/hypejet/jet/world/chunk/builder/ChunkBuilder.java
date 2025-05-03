package net.hypejet.jet.world.chunk.builder;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a builder of {@linkplain Chunk a chunk}.
 *
 * @since 1.0
 * @see Chunk
 */
public interface ChunkBuilder {
    /**
     * Sets a block at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified to have
     * {@linkplain BlockState a block state} specified.
     *
     * @param position the chunk-relative block position
     * @param blockState the block state
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder setBlockState(@NonNull ChunkRelativeBlockPosition position, @NonNull BlockState blockState);

    /**
     * Sets a block at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} specified to have
     * a block entity with data specified.
     *
     * @param position the chunk-relative block position
     * @param blockEntityData the block entity data
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder setBlockEntity(@NonNull ChunkRelativeBlockPosition position,
                                         @NonNull CompoundBinaryTag blockEntityData);

    /**
     * Sets {@linkplain Biome a biome} at {@linkplain ChunkRelativeBiomePosition a chunk-relative biome position}
     * to be replaced with a biome speciifed.
     *
     * @param position the chunk-relative biome position
     * @param biome a registry entry of the biome
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder setBiome(@NonNull ChunkRelativeBiomePosition position, @NonNull RegistryEntry<Biome> biome);

    /**
     * Sets a skylight level at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} to a value
     * specified.
     *
     * @param position the chunk-relative block position
     * @param level the value
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder setSkyLightLevel(@NonNull ChunkRelativeBlockPosition position, byte level);

    /**
     * Sets a block light level at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} to a value
     * specified.
     *
     * @param position the chunk-relative block position
     * @param level the value
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder setBlockLightLevel(@NonNull ChunkRelativeBlockPosition position, byte level);

    /**
     * Creates {@linkplain Chunk a chunk} from this builder.
     *
     * @return the chunk
     * @since 1.0
     */
    @NonNull Chunk build();
}