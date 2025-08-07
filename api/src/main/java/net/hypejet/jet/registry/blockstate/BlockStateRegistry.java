package net.hypejet.jet.registry.blockstate;

import net.hypejet.jet.registry.holder.Holder;
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
     * Gets a default {@linkplain BlockState block state} of the specified {@linkplain BlockType block type}.
     *
     * @param blockType the holder referencing to the block type
     * @return the block state
     * @since 1.0
     */
    @NonNull BlockState defaultBlockState(Holder.@NonNull Reference<BlockType> blockType);

    /**
     * Gets a {@linkplain BlockState block state} (which belongs to the specified {@linkplain BlockType block type})
     * with the specified properties.
     *
     * @param blockType the holder referencing to the block type
     * @param properties the properties
     * @return the block state
     * @since 1.0
     */
    @NonNull BlockState blockState(Holder.@NonNull Reference<BlockType> blockType,
                                   @NonNull Map<String, String> properties);
}