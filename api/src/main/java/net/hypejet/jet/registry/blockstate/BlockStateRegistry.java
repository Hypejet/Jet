package net.hypejet.jet.registry.blockstate;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.block.state.BlockState;
import net.hypejet.jet.world.block.BlockType;
import net.hypejet.jet.world.block.state.BlockStateReference;
import org.jspecify.annotations.NullMarked;

/**
 * A registry of {@linkplain BlockState block states}.
 *
 * @since 1.0
 * @see BlockState
 */
@NullMarked
public interface BlockStateRegistry {
    /**
     * Gets a default {@linkplain BlockState block state} of the specified {@linkplain BlockType block type}.
     *
     * @param blockType the holder referencing to the block type
     * @return the block state
     * @since 1.0
     */
    BlockState defaultBlockState(Holder.Reference<BlockType> blockType);

    /**
     * Gets a {@linkplain BlockState block state} that the specified
     * {@linkplain BlockStateReference block state reference} references to.
     *
     * @param reference the block state reference
     * @return the referenced block state
     * @throws IllegalArgumentException if there is no block state that matches with
     *                                  data of the specified block state reference
     * @since 1.0
     */
    BlockState blockState(BlockStateReference reference);
}