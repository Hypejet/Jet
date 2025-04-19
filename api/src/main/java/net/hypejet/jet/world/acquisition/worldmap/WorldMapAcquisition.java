package net.hypejet.jet.world.acquisition.worldmap;

import net.hypejet.concurrency.Acquisition;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.coordinate.BiomePosition;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * Represents an acquisition, which allows getting guarded map data
 * of {@linkplain net.hypejet.jet.world.World a world}.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.World
 */
public interface WorldMapAcquisition extends Acquisition {
    /**
     * Gets {@linkplain RegistryEntry a registry entry} of type of block at {@linkplain BlockPosition a block position}
     * specified. Returns {@code null} if {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated
     * with the block position has not been loaded.
     *
     * @param position the block position
     * @return the registry entry, {@code null} if the chunk has not been loaded
     * @since 1.0
     */
    @Nullable RegistryEntry<?> getOptionalBlockType(@NonNull BlockPosition position);

    /**
     * Gets {@linkplain Map a map} of properties of a block at {@linkplain BlockPosition a block position} specified.
     * Returns {@code null} if {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the block
     * position has not been loaded.
     *
     * @param position the block position
     * @return the map, {@code null} if the chunk has not been loaded
     * @since 1.0
     */
    @Nullable Map<String, String> getOptionalBlockProperties(@NonNull BlockPosition position);

    /**
     * Gets {@linkplain RegistryEntry a registry entry} of {@linkplain Biome a biome}
     * at {@linkplain BiomePosition a biome position} specified. Returns {@code null}
     * if {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the biome position
     * has not been loaded.
     *
     * @param position the biome position
     * @return the registry entry, {@code null} if the chunk has not been loaded
     * @since 1.0
     */
    @Nullable RegistryEntry<Biome> getOptionalBiome(@NonNull BiomePosition position);

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