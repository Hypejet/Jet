package net.hypejet.jet.data.json.model.block;

import com.google.common.primitives.ImmutableIntArray;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * A Minecraft block.
 *
 * @param requiredFeatureFlags a set of required feature flags to enable this block
 * @param defaultBlockStateId an identifier of the default state of this block
 * @param blockStateIds an array of all possible states of this block
 * @since 1.0
 */
public record JsonBlock(@NonNull Set<Key> requiredFeatureFlags, int defaultBlockStateId,
                        @NonNull ImmutableIntArray blockStateIds) {
    /**
     * Constructs the {@linkplain JsonBlock block}.
     *
     * @param requiredFeatureFlags a set of required feature flags to enable this block
     * @param defaultBlockStateId an identifier of the default state of this block
     * @param blockStateIds an array of all possible states of this block
     * @since 1.0
     */
    public JsonBlock {
        Objects.requireNonNull(requiredFeatureFlags, "required feature flags");
        Objects.requireNonNull(blockStateIds, "block state ids");
    }
}
