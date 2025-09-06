package net.hypejet.jet.server.entity;

import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.server.world.block.entity.JetBlockEntityType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * An implementation of the {@linkplain EntityType entity type}.
 *
 * @param requiredFeatureFlags a set of feature flag keys required to enable this entity type
 * @param maxAirSupply a maximum air supply that entities of this type have
 * @since 1.0
 */
public record JetEntityType(@NonNull Set<Key> requiredFeatureFlags, int maxAirSupply) implements EntityType {
    /**
     * Constructs the {@linkplain JetBlockEntityType block entity type}.
     *
     * @param requiredFeatureFlags a set of feature flag keys that should be
     *                             required to enable the constructed entity type
     * @param maxAirSupply a maximum air supply that entities of the constructed entity type should have
     * @since 1.0
     */
    public JetEntityType {
        requiredFeatureFlags = Set.copyOf(Objects.requireNonNull(requiredFeatureFlags, "required feature flags"));
    }

    /**
     * Converts the specified {@linkplain JsonEntityType Jet data entity type} to a Jet equivalent.
     *
     * @param entityType the entity type to convert
     * @return the converted entity type
     * @since 1.0
     */
    public static @NonNull JetEntityType convert(@NonNull JsonEntityType entityType) {
        return new JetEntityType(entityType.requiredFeatureFlags(), entityType.maxAirSupply());
    }
}