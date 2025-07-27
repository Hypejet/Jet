package net.hypejet.jet.data.json.model.block;

import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * A Minecraft block state.
 *
 * @param properties properties of this block state
 * @param isAir whether this block state should be recognised as an air
 * @param hasFluidState whether this block state has a fluid state associated with it, in other words whether
 *                      the block state represents a fluid
 * @param blocksMotion whether this block state blocks motion of players entering it
 * @since 1.0
 */
public record JsonBlockState(@NonNull Map<String, String> properties, boolean isAir,
                             boolean hasFluidState, boolean blocksMotion) {
    /**
     * Constructs the {@linkplain JsonBlockState block state}.
     *
     * @param properties properties of this block state
     * @param isAir whether this block state should be recognised as an air
     * @param hasFluidState whether this block state has a fluid state associated with it, in other words whether
     *                      the block state represents a fluid
     * @param blocksMotion whether this block state blocks motion of players entering it
     * @since 1.0
     */
    public JsonBlockState {
        properties = Map.copyOf(Objects.requireNonNull(properties, "properties"));
    }
}