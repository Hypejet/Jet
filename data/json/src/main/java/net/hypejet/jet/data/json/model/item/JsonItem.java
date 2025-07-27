package net.hypejet.jet.data.json.model.item;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * A Minecraft item.
 *
 * @param requiredFeatureFlags a set of keys of required feature flags to enable the item
 * @since 1.0
 */
public record JsonItem(@NonNull Set<Key> requiredFeatureFlags) {
    /**
     * Constructs the {@linkplain JsonItem item}.
     *
     * @param requiredFeatureFlags a set of keys of required feature flags to enable the item
     * @since 1.0
     */
    public JsonItem {
        requiredFeatureFlags = Set.copyOf(Objects.requireNonNull(requiredFeatureFlags, "required feature flags"));
    }
}