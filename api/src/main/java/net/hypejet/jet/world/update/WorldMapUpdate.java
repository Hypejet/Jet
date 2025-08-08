package net.hypejet.jet.world.update;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.coordinate.biome.BiomePosition;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a builder of an update of map data of {@linkplain net.hypejet.jet.world.World a world}.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.World
 */
public interface WorldMapUpdate {
    /**
     * Sets {@linkplain BlockState a block state} of a block at {@linkplain BlockPosition a block position} specified
     * to be updated with a value specified.
     *
     * <p>Note that a block entity associated with the block will be removed if block types of previous block
     * state and the block state specified are different.</p>
     *
     * @param position the block position
     * @param blockState the value
     * @return this builder
     * @since 1.0
     */
    @NonNull WorldMapUpdate updateBlockState(@NonNull BlockPosition position, @NonNull BlockState blockState);

    /**
     * Sets block entity data of a block at {@linkplain BlockPosition a block position} specified to be updated
     * with a value specified.
     *
     * @param position the block position
     * @param blockEntityData the value
     * @return this builder
     * @since 1.0
     */
    @NonNull WorldMapUpdate updateBlockEntity(@NonNull BlockPosition position,
                                              @NonNull CompoundBinaryTag blockEntityData);

    /**
     * Sets a {@linkplain Biome biome} at the specified {@linkplain BiomePosition biome position}
     * to be updated with value referenced by the specified {@linkplain Holder.Reference holder}.
     *
     * @param position the biome position
     * @param biome the biome
     * @return this builder
     * @since 1.0
     */
    @NonNull WorldMapUpdate updateBiome(@NonNull BiomePosition position, Holder.@NonNull Reference<Biome> biome);

    /**
     * Sets a skylight level at {@linkplain BlockPosition a block position} specified to be updated
     * with a value specified.
     *
     * @param position the block position
     * @param level the value
     * @return this builder
     * @since 1.0
     */
    @NonNull WorldMapUpdate updateSkyLightLevel(@NonNull BlockPosition position, byte level);

    /**
     * Sets a block light level at {@linkplain BlockPosition a block position} specified to be updated
     * with a value specified.
     *
     * @param position the block position
     * @param level the value
     * @return this builder
     * @since 1.0
     */
    @NonNull WorldMapUpdate updateBlockLightLevel(@NonNull BlockPosition position, byte level);

    /**
     * Performs the update.
     *
     * @since 1.0
     */
    void update();
}