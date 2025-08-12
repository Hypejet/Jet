package net.hypejet.jet.world.acquisition.worldmap;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.biome.BiomePosition;
import net.hypejet.jet.world.update.WorldMapUpdate;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain WorldMapAcquisition a world-map acquisition}, which allows changing the world data.
 *
 * @since 1.0
 * @see WorldMapAcquisition
 */
public interface WriteWorldMapAcquisition extends WorldMapAcquisition {
    /**
     * Gets {@linkplain BlockState a block state} of a block at {@linkplain BlockPosition a block position} specified.
     *
     * <p>If {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the block position has not been
     * loaded, it is going to be loaded.</p>
     *
     * @param position the block position
     * @return the block state
     * @since 1.0
     */
    @NonNull BlockState getBlockState(@NonNull BlockPosition position);

    /**
     * Gets a {@linkplain Holder.Reference holder referencing to} a {@linkplain Biome biome}
     * present at a {@linkplain BiomePosition biome position} specified.
     *
     * <p>If {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated
     * with the biome position has not been loaded, it is going to be loaded.</p>
     *
     * @param position the biome position
     * @return the biome holder
     * @since 1.0
     */
    Holder.@NonNull Reference<Biome> getBiome(@NonNull BiomePosition position);

    /**
     * Gets data of a block entity of a block at {@linkplain BlockPosition a block position} specified
     *
     * <p>If {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the block position has not been
     * loaded, it is going to be loaded.</p>
     *
     * @param position the block position
     * @return the block entity data, {@code null} if the block does not have a block entity
     * @since 1.0
     */
    @Nullable CompoundBinaryTag getBlockEntity(@NonNull BlockPosition position);

    /**
     * Gets level of skylight at {@linkplain BlockPosition a block position} specified.
     *
     * <p>If {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the block position has not been
     * loaded, it is going to be loaded.</p>
     *
     * @param position the block position
     * @return the level
     * @since 1.0
     */
    byte getSkyLightValue(@NonNull BlockPosition position);

    /**
     * Gets level of block light at {@linkplain BlockPosition a block position} specified.
     *
     * <p>If {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the block position has not been
     * loaded, it is going to be loaded.</p>
     *
     * @param position the block position
     * @return the level
     * @since 1.0
     */
    byte getBlockLightValue(@NonNull BlockPosition position);

    /**
     * Creates {@linkplain WorldMapUpdate a world-map update builder} of a world map
     * of {@linkplain net.hypejet.jet.world.World a world} associated with this acquisition.
     *
     * @return the world-map update builder
     * @since 1.0
     */
    @NonNull WorldMapUpdate createUpdate();
}