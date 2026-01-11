package net.hypejet.jet.data.json.model.block;

import com.google.common.primitives.ImmutableIntArray;
import net.hypejet.jet.data.json.model.block.state.property.JsonStateProperty;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A Minecraft block.
 *
 * @param requiredFeatureFlags a set of required feature flags to enable this block
 * @param defaultBlockStateProperties a map associating block state property names with string representations
 *                                    of values that these properties have when using default block state of this block
 * @param blockStateIds an array of all possible states of this block
 * @param blockStateProperties a map associating block state property names with block state properties of this block
 * @since 1.0
 */
@NullMarked
public record JsonBlock(Set<Key> requiredFeatureFlags, Map<String, String> defaultBlockStateProperties,
                        ImmutableIntArray blockStateIds, Map<String, JsonStateProperty> blockStateProperties) {
    /**
     * Constructs the {@linkplain JsonBlock block}.
     *
     * @param requiredFeatureFlags a set of required feature flags to enable this block
     * @param defaultBlockStateProperties a map associating block state property names with string representations
     *                                    of values that these properties have assigned in the default block state
     *                                    of the constructed block
     * @param blockStateIds an array of all possible states of this block
     * @param blockStateProperties a map associating block state property names
     *                             with block state properties that the block should have
     * @since 1.0
     */
    public JsonBlock {
        Objects.requireNonNull(requiredFeatureFlags, "required feature flags");
        Objects.requireNonNull(blockStateIds, "block state ids");
        blockStateProperties = Map.copyOf(Objects.requireNonNull(blockStateProperties, "block state properties"));
    }
}