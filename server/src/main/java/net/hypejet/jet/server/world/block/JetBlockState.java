package net.hypejet.jet.server.world.block;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.registry.JetRegistryEntry;
import net.hypejet.jet.world.block.BlockState;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents an implementation of {@linkplain BlockState a block state}.
 *
 * @param blockType a block type that the block state belongs to
 * @param properties a properties of the block state
 * @param isAir whether the state should be recognised as an air
 * @param hasFluidState whether the state has a fluid state associated with it, in other words whether the block state
 *                      represents a fluid
 * @param blocksMotion whether motion of players entering a block with this block state is be blocked
 * @since 1.0
 */
public record JetBlockState(@NonNull JetRegistryEntry<JetBlockType> blockType, @NonNull Map<String, String> properties,
                            boolean isAir, boolean hasFluidState, boolean blocksMotion)
        implements BlockState {
    /**
     * Constructs the {@linkplain JetBlockState block state implementation}.
     *
     * @param blockType a block type that the block state belongs to
     * @param properties a properties of the block state
     * @param isAir whether the state should be recognised as an air
     * @param hasFluidState whether the state has a fluid state associated with it, in other words whether the block state
     *                      represents a fluid
     * @param blocksMotion whether motion of players entering a block with this block state is be blocked
     * @since 1.0
     */
    public JetBlockState {
        NullabilityUtil.requireNonNull(blockType, "block type");
        properties = Map.copyOf(NullabilityUtil.requireNonNull(properties, "properties"));
    }
}