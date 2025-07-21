package net.hypejet.jet.data.json.model.entity;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * A type of Minecraft entity.
 *
 * @param requiredFeatureFlags a set of keys of required flags to enable this entity type
 * @since 1.0
 */
public record JsonEntityType(@NonNull Set<Key> requiredFeatureFlags) {
    /**
     * Constructs the {@linkplain JsonEntityType entity type}.
     *
     * @param requiredFeatureFlags a set of keys of required flags to enable this entity type
     * @since 1.0
     */
    public JsonEntityType {
        requiredFeatureFlags = Set.copyOf(Objects.requireNonNull(requiredFeatureFlags, "required feature flags"));
    }
}