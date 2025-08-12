package net.hypejet.jet.server.world.block;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.block.BlockType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * Represents an implementation of {@linkplain BlockState a block state}.
 *
 * @param blockType a holder of block type that the block state belongs to
 * @param properties a properties of the block state
 * @param isAir whether the state should be recognised as an air
 * @param hasFluidState whether the state has a fluid state associated with it, in other words whether the block state
 *                      represents a fluid
 * @param blocksMotion whether motion of players entering a block with this block state is be blocked
 * @param isLeaves whether block that this block state is associated with is leaves
 * @since 1.0
 */
public record JetBlockState(Holder.@NonNull Reference<BlockType> blockType, @NonNull Map<String, String> properties,
                            boolean isAir, boolean hasFluidState, boolean blocksMotion, boolean isLeaves)
        implements BlockState {
    /**
     * Constructs the {@linkplain JetBlockState block state implementation}.
     *
     * @param blockType a holder of block type that the block state belongs to
     * @param properties a properties of the block state
     * @param isAir whether the state should be recognised as an air
     * @param hasFluidState whether the state has a fluid state associated with it, in other words whether the block state
     *                      represents a fluid
     * @param blocksMotion whether motion of players entering a block with this block state is be blocked
     * @param isLeaves whether block that the block state is going to be associated with is leaves
     * @since 1.0
     */
    public JetBlockState {
        Objects.requireNonNull(blockType, "block type");
        properties = Map.copyOf(Objects.requireNonNull(properties, "properties"));
    }
}