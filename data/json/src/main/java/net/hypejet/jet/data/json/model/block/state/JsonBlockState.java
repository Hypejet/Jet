package net.hypejet.jet.data.json.model.block.state;

import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * A Minecraft block state.
 *
 * @param properties a map associating block state property names with string representations
 *                   of values that these properties have when using this block state
 * @param isAir whether this block state should be recognised as an air
 * @param hasFluidState whether this block state has a fluid state associated with it, in other words whether
 *                      the block state represents a fluid
 * @param blocksMotion whether this block state blocks motion of players entering it
 * @param isLeaves whether block that this block state is associated with is leaves
 * @since 1.0
 */
public record JsonBlockState(@NonNull Map<String, String> properties, boolean isAir,
                             boolean hasFluidState, boolean blocksMotion, boolean isLeaves) {
    /**
     * Constructs the {@linkplain JsonBlockState block state}.
     *
     * @param properties a map associating block state property names with string representations
     *                   of values that these properties should have, these properties will be
     *                   used when using the constructed block state
     * @param isAir whether this block state should be recognised as an air
     * @param hasFluidState whether this block state has a fluid state associated with it, in other words whether
     *                      the block state represents a fluid
     * @param blocksMotion whether this block state blocks motion of players entering it
     * @param isLeaves whether block that the block state is going to be associated with is leaves
     * @since 1.0
     */
    public JsonBlockState {
        properties = Map.copyOf(Objects.requireNonNull(properties, "properties"));
    }
}