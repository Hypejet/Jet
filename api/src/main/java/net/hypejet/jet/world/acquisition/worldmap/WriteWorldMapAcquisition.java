package net.hypejet.jet.world.acquisition.worldmap;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.block.entity.BlockEntity;
import net.hypejet.jet.world.coordinate.BiomePosition;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.update.WorldMapUpdate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * Represents {@linkplain WorldMapAcquisition a world-map acquisition}, which allows changing the world data.
 *
 * @since 1.0
 * @see WorldMapAcquisition
 */
public interface WriteWorldMapAcquisition extends WorldMapAcquisition {
    /**
     * Gets {@linkplain RegistryEntry a registry entry} of type of block at {@linkplain BlockPosition a block position}
     * specified.
     *
     * <p>If {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the block position has not been
     * loaded, it is going to be loaded.</p>
     *
     * @param position the block position
     * @return the registry entry
     * @since 1.0
     */
    @NonNull RegistryEntry<?> getBlockType(@NonNull BlockPosition position);

    /**
     * Gets {@linkplain Map a map} of properties of a block at {@linkplain BlockPosition a block position} specified.
     *
     * <p>If {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the block position has not been
     * loaded, it is going to be loaded.</p>
     *
     * @param position the block position
     * @return the map
     * @since 1.0
     */
    @NonNull Map<String, String> getBlockProperties(@NonNull BlockPosition position);

    /**
     * Gets {@linkplain RegistryEntry a registry entry} of {@linkplain Biome a biome}
     * at {@linkplain BiomePosition a biome position} specified.
     *
     * <p>>If {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the biome position
     * has not been loaded, it is going to be loaded.</p>
     *
     * @param position the biome position
     * @return the registry entry
     * @since 1.0
     */
    @NonNull RegistryEntry<Biome> getBiome(@NonNull BiomePosition position);

    /**
     * Gets {@linkplain BlockEntity a block entity} of a block at {@linkplain BlockPosition a block position}
     * specified
     *
     * <p>If {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} associated with the block position has not been
     * loaded, it is going to be loaded.</p>
     *
     * @param position the block position
     * @return the block entity
     * @since 1.0
     */
    @Nullable BlockEntity getBlockEntity(@NonNull BlockPosition position);

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