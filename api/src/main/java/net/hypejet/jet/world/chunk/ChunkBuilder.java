package net.hypejet.jet.world.chunk;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.block.entity.BlockEntity;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * Represents a builder of a chunk of {@linkplain net.hypejet.jet.world.World a world}.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.World
 */
public interface ChunkBuilder {
    /**
     * Sets a block at a chunk-relative block coordinate with {@linkplain Key a key} specified of a type, of which
     * the block should be. The block is going to have default properties and no {@linkplain BlockEntity block entity}.
     *
     * @param x a chunk-relative {@code X} value of the coordinate
     * @param y an absolute {@code Y} value of the coordinate
     * @param z a chunk-relative {@code Z} value of the coordinate
     * @param blockTypeKey the key of the block type
     * @since 1.0
     */
    void setBlock(byte x, short y, byte z, @NonNull Key blockTypeKey);

    /**
     * Sets a block at a chunk-relative block coordinate with values specified. The block is going to have default
     * properties.
     *
     * @param x a chunk-relative {@code X} value of the coordinate
     * @param y an absolute {@code Y} value of the coordinate
     * @param z a chunk-relative {@code Z} value of the coordinate
     * @param blockTypeKey a key of a block type, of which the block should be
     * @param blockEntity a block entity that the block should have, {@code null} if none
     * @since 1.0
     */
    void setBlock(byte x, short y, byte z, @NonNull Key blockTypeKey, @Nullable BlockEntity blockEntity);

    /**
     * Sets a block at a chunk-relative block coordinate with values specified and no block entity.
     *
     * @param x a chunk-relative {@code X} value of the coordinate
     * @param y an absolute {@code Y} value of the coordinate
     * @param z a chunk-relative {@code Z} value of the coordinate
     * @param blockTypeKey a key of a block type, of which the block should be
     * @param properties a properties that the block should have, {@code null} if default properties should be used
     * @since 1.0
     */
    void setBlock(byte x, short y, byte z, @NonNull Key blockTypeKey, @Nullable Map<String, String> properties);

    /**
     * Sets a block at a chunk-relative block coordinate with values specified.
     *
     * @param x a chunk-relative {@code X} value of the coordinate
     * @param y an absolute {@code Y} value of the coordinate
     * @param z a chunk-relative {@code Z} value of the coordinate
     * @param blockTypeKey a key of a block type, of which the block should be
     * @param properties a properties that the block should have, {@code null} if default properties should be used
     * @param blockEntity a block entity that the block should have, {@code null} if none
     * @since 1.0
     */
    void setBlock(byte x, short y, byte z, @NonNull Key blockTypeKey, @Nullable Map<String, String> properties,
                  @Nullable BlockEntity blockEntity);

    /**
     * Sets {@linkplain Biome a biome} at a chunk-relative biome coordinate with values specified.
     *
     * @param x a chunk-relative {@code X} value of the coordinate
     * @param y an absolute {@code Y} value of the coordinate
     * @param z a chunk-relative {@code Z} value of the coordinate
     * @param biome a registry entry of a biome that should be set at the coordinate specified
     * @since 1.0
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