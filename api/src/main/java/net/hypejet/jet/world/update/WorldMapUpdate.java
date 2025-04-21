package net.hypejet.jet.world.update;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.coordinate.BiomePosition;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * Represents a builder of an update of map data of {@linkplain net.hypejet.jet.world.World a world}.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.World
 */
public interface WorldMapUpdate {
    /**
     * Sets block state of a block at {@linkplain BlockPosition a block position} specified to be updated with
     * a default block state of a block type with {@linkplain Key a key} specified.
     *
     * @param position the block position
     * @param blockTypeKey the block type key
     * @return this builder
     * @since 1.0
     */
    @NonNull WorldMapUpdate updateBlockState(@NonNull BlockPosition position, @NonNull Key blockTypeKey);

    /**
     * Sets block state of a block at {@linkplain BlockPosition a block position} specified to be updated with
     * a block state with block properties specified and a block type with {@linkplain Key a key} specified.
     *
     * @param position the block position
     * @param blockTypeKey the block type key
     * @param properties the properties, {@code null} to use default properties of the block type
     * @return this builder
     * @since 1.0
     */
    @NonNull WorldMapUpdate updateBlockState(@NonNull BlockPosition position, @NonNull Key blockTypeKey,
                                             @Nullable Map<String, String> properties);

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
     * Sets {@linkplain Biome a biome} at {@linkplain BiomePosition a biome position} specified to be updated
     * with value of {@linkplain RegistryEntry a registry entry} specified.
     *
     * @param position the biome position
     * @param biome the biome
     * @return this builder
     * @since 1.0
     */
    @NonNull WorldMapUpdate updateBiome(@NonNull BiomePosition position, @NonNull RegistryEntry<Biome> biome);

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