package net.hypejet.jet.data.json.model.entity;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * A type of Minecraft entity.
 *
 * @param requiredFeatureFlags a set of keys of required feature flags to enable this entity type
 * @param maxAirSupply a maximum air supply that entities of this type have
 * @since 1.0
 */
public record JsonEntityType(@NonNull Set<Key> requiredFeatureFlags, int maxAirSupply) {
    /**
     * Constructs the {@linkplain JsonEntityType entity type}.
     *
     * @param requiredFeatureFlags a set of keys of feature flags that should be required
     *                             to enable the entity type that is being constructed
     * @param maxAirSupply a maximum air supply that entities of the constructed entity type should have
     * @since 1.0
     */
    public JsonEntityType {
        requiredFeatureFlags = Set.copyOf(Objects.requireNonNull(requiredFeatureFlags, "required feature flags"));
    }
}