package net.hypejet.jet.world.chunk.factory.palette;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain ChunkPaletteFactory a chunk palette factory}, which creates
 * {@linkplain net.hypejet.jet.world.chunk.section.ChunkPalette chunk palettes}, which store block states. The factory
 * also provides methods for management of the block states.
 *
 * @param <BS> a type of elements representing block states
 * @since 1.0
 * @see net.hypejet.jet.world.chunk.section.ChunkPalette
 * @see ChunkPaletteFactory
 */
public interface BlockStateChunkPaletteFactory<BS> extends ChunkPaletteFactory<BS> {
    /**
     * Gets a block state with properties specified, which is associated with a block type with a key specified.
     *
     * @param blockTypeKey the key
     * @param properties the properties
     * @return the block state
     * @since 1.0
     */
    @NonNull BS blockState(@NonNull Key blockTypeKey, @NonNull Map<String, String> properties);

    /**
     * Gets {@linkplain Key a key} of a block type that a block state specified is associated with.
     *
     * @param blockState the block state
     * @return the key
     * @since 1.0
     */
    @NonNull Key blockTypeKey(@NonNull BS blockState);

    /**
     * Gets {@linkplain Map a map} of properties of a block state specified.
     *
     * @param blockState the block state
     * @return the map
     * @since 1.0
     */
    @NonNull Map<String, String> blockProperties(@NonNull BS blockState);

    /**
     * Gets a default block state for a block type with {@linkplain Key a key} specified.
     *
     * @param blockTypeKey the key
     * @return the default block state
     * @since 1.0
     */
    @NonNull BS defaultBlockState(@NonNull Key blockTypeKey);
}