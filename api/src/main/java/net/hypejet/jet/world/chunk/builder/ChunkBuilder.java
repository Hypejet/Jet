package net.hypejet.jet.world.chunk.builder;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.state.BlockState;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NonNull;

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
     * <p>Note that a block entity associated with the block will be removed if block types of previous block
     * state and the block state specified are different.</p>
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
     * to be replaced with a biome specified.
     *
     * @param position the chunk-relative biome position
     * @param biome a holder referencing to the biome that should be present at the specified position
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder setBiome(@NonNull ChunkRelativeBiomePosition position,
                                   Holder.@NonNull Reference<Biome> biome);

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