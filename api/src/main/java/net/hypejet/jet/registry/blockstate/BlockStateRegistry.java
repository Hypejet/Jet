package net.hypejet.jet.registry.blockstate;

import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.block.BlockType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents a registry of {@linkplain BlockState block states}.
 *
 * @since 1.0
 * @see BlockState
 */
public interface BlockStateRegistry {
    /**
     * Gets a default {@linkplain BlockState block state} of {@linkplain BlockType a block type} specified.
     *
     * @param blockType a registry entry of the block type
     * @return the block state
     * @since 1.0
     */
    @NonNull BlockState defaultBlockState(@NonNull RegistryEntry<? extends BlockType> blockType);

    /**
     * Gets {@linkplain BlockState a block state} of {@linkplain BlockType a block type} specified with properties
     * specified.
     *
     * @param blockType a registry entry block type
     * @param properties the properties
     * @return the block state
     * @since 1.0
     */
    @NonNull BlockState blockState(@NonNull RegistryEntry<? extends BlockType> blockType,
                                   @NonNull Map<String, String> properties);
}