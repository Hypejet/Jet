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
 * @param living whether this is a type of living entity
 * @param mob whether this is a type of mob entity
 * @param ageableMob whether this is a type of ageable mob entity
 * @since 1.0
 */
public record JsonEntityType(@NonNull Set<Key> requiredFeatureFlags, int maxAirSupply,
                             boolean living, boolean mob, boolean ageableMob) {
    /**
     * Constructs the {@linkplain JsonEntityType entity type}.
     *
     * @param requiredFeatureFlags a set of keys of feature flags that should be required
     *                             to enable the entity type that is being constructed
     * @param maxAirSupply a maximum air supply that entities of the constructed entity type should have
     * @param living whether the entity type should be a type of living entity
     * @param mob whether the entity type should be a type of mob entity
     * @param ageableMob whether the entity type should be a type of ageable mob entity
     * @since 1.0
     */
    public JsonEntityType {
        requiredFeatureFlags = Set.copyOf(Objects.requireNonNull(requiredFeatureFlags, "required feature flags"));
    }
}
