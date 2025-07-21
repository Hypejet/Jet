package net.hypejet.jet.data.generator.adpater;

import com.google.common.primitives.ImmutableIntArray;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain Block blocks} to a Jet data equivalent.
 *
 * @since 1.0
 * @see Block
 */
public final class BlockAdapter {

    private BlockAdapter() {}

    /**
     * Converts the specified {@linkplain Block block} to a Jet data equivalent.
     *
     * @param block the block to convert
     * @return the converted block
     * @since 1.0
     */
    public static @NonNull JsonBlock convert(@NonNull Block block) {
        return new JsonBlock(
                KeyAdapter.convertSet(FeatureFlags.REGISTRY.toNames(block.requiredFeatures())),
                Block.getId(block.defaultBlockState()),
                convertStates(block.getStateDefinition())
        );
    }

    private static @NonNull ImmutableIntArray convertStates(@NonNull StateDefinition<Block, BlockState> definition) {
        ImmutableIntArray.Builder builder = ImmutableIntArray.builder();
        for (BlockState state : definition.getPossibleStates())
            builder.add(Block.getId(state));
        return builder.build();
    }
}