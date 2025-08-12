package net.hypejet.jet.world.acquisition.worldmap;

import net.hypejet.concurrency.Acquisition;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.biome.BiomePosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an acquisition, which allows getting guarded map data
 * of {@linkplain net.hypejet.jet.world.World a world}.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.World
 */
public interface WorldMapAcquisition extends Acquisition {
    /**
     * Gets {@linkplain BlockState a block state} of a block at {@linkplain BlockPosition a block position} specified.
     * Returns {@code null} if {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with
     * the block position has not been loaded.
     *
     * @param position the block position
     * @return the block state, {@code null} if the chunk has not been loaded
     * @since .10
     */
    @Nullable BlockState getOptionalBlockState(@NonNull BlockPosition position);

    /**
     * Gets a {@linkplain Holder.Reference holder referencing to} a {@linkplain Biome biome}
     * present at a {@linkplain BiomePosition biome position} specified. Returns {@code null}
     * if a {@linkplain net.hypejet.jet.world.chunk.Chunk chunk} associated
     * with the specified biome position has not been loaded.
     *
     * @param position the biome position
     * @return the biome holder, {@code null} if the chunk has not been loaded
     * @since 1.0
     */
    Holder.@Nullable Reference<Biome> getOptionalBiome(@NonNull BiomePosition position);

    /**
     * Gets data of a block entity of a block at {@linkplain BlockPosition a block position}
     * specified. Returns {@code null} if {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated
     * with the block position has not been loaded or the block does not have a block entity.
     *
     * @param position the block position
     * @return the block entity data, {@code null} if the chunk has not been loaded or the block does not have
     *         a block entity
     * @since 1.0
     */
    @Nullable CompoundBinaryTag getOptionalBlockEntity(@NonNull BlockPosition position);

    /**
     * Gets level of skylight at {@linkplain BlockPosition a block position} specified. Returns {@link Byte#MIN_VALUE}
     * if {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the block position has not been
     * loaded.
     *
     * @param position the block position
     * @return the level, {@link Byte#MIN_VALUE} if the chunk has not been loaded
     * @since 1.0
     */
    byte getOptionalSkyLightLevel(@NonNull BlockPosition position);

    /**
     * Gets level of block light at {@linkplain BlockPosition a block position} specified.
     * Returns {@link Byte#MIN_VALUE} if {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated
     * with the block position has not been loaded.
     *
     * @param position the block position
     * @return the level, {@link Byte#MIN_VALUE} if the chunk has not been loaded
     * @since 1.0
     */
    byte getOptionalBlockLightLevel(@NonNull BlockPosition position);
}