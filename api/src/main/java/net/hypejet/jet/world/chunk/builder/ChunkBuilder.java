package net.hypejet.jet.world.chunk.builder;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.block.entity.BlockEntity;
import net.hypejet.jet.world.chunk.Chunk;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBiomePosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * Represents a builder of {@linkplain Chunk a chunk}.
 *
 * @param <BS> a type of block state implementation that block-state chunk palettes of chunk sections should use
 * @since 1.0
 * @see Chunk
 */
public interface ChunkBuilder<BS> {
    /**
     * Sets a block at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} to have default block
     * data of a block type with {@linkplain Key a key} specified.
     *
     * @param position the chunk-relative block position
     * @param blockTypeKey the key
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder<BS> setBlock(@NonNull ChunkRelativeBlockPosition position, @NonNull Key blockTypeKey);

    /**
     * Sets a block at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} to have default block
     * data of a block type with {@linkplain Key a key} specified. The block is also going to have
     * {@linkplain BlockEntity a block entity} specified.
     *
     * @param position the chunk-relative block position
     * @param blockTypeKey the key
     * @param blockEntity the block entity, {@code null} if the block should not have a block entity
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder<BS> setBlock(@NonNull ChunkRelativeBlockPosition position, @NonNull Key blockTypeKey,
                                       @Nullable BlockEntity blockEntity);

    /**
     * Sets a block at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} to have a block
     * data with block properties specified and a block type with {@linkplain Key a key} specified
     *
     * @param position the chunk-relative block position
     * @param blockTypeKey the key
     * @param properties the block properties, {@code null} to use default properties of the block type
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder<BS> setBlock(@NonNull ChunkRelativeBlockPosition position, @NonNull Key blockTypeKey,
                                       @Nullable Map<String, String> properties);

    /**
     * Sets a block at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} to have a block
     * data with block properties specified and a block type with {@linkplain Key a key} specified. The block
     * is also going to have {@linkplain BlockEntity a block entity} specified.
     *
     * @param position the chunk-relative block position
     * @param blockTypeKey the key
     * @param properties the block properties, {@code null} to use default properties of the block type
     * @param blockEntity the block entity, {@code null} if the block should not have a block entity
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder<BS> setBlock(@NonNull ChunkRelativeBlockPosition position, @NonNull Key blockTypeKey,
                                       @Nullable Map<String, String> properties, @Nullable BlockEntity blockEntity);

    /**
     * Sets {@linkplain Biome a biome} at {@linkplain ChunkRelativeBiomePosition a chunk-relative biome position}
     * to be replaced with a biome speciifed.
     *
     * @param position the chunk-relative biome position
     * @param biome a registry entry of the biome
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder<BS> setBiome(@NonNull ChunkRelativeBiomePosition position,
                                       @NonNull RegistryEntry<Biome> biome);

    /**
     * Sets a skylight level at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} to a value
     * specified.
     *
     * @param position the chunk-relative block position
     * @param level the value
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder<BS> setSkyLightLevel(@NonNull ChunkRelativeBlockPosition position, byte level);

    /**
     * Sets a block light level at {@linkplain ChunkRelativeBlockPosition a chunk-relative block position} to a value
     * specified.
     *
     * @param position the chunk-relative block position
     * @param level the value
     * @return this builder
     * @since 1.0
     */
    @NonNull ChunkBuilder<BS> setBlockLightLevel(@NonNull ChunkRelativeBlockPosition position, byte level);

    /**
     * Creates {@linkplain Chunk a chunk} from this builder.
     *
     * @return the chunk
     * @since 1.0
     */
    @NonNull Chunk<BS> build();
}