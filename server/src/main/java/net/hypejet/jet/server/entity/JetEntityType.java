package net.hypejet.jet.server.entity;

import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.hypejet.jet.entity.EntityType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * An implementation of the {@linkplain EntityType entity type}.
 *
 * @param requiredFeatureFlags a set of feature flag keys required to enable this entity type
 * @param maxAirSupply a maximum air supply that entities of this type have
 * @param living whether this is a type of living entity
 * @since 1.0
 */
public record JetEntityType(@NonNull Set<Key> requiredFeatureFlags, int maxAirSupply, boolean living)
        implements EntityType {
    /**
     * Constructs the {@linkplain JetEntityType entity type implementation}.
     *
     * @param requiredFeatureFlags a set of feature flag keys that should be
     *                             required to enable the constructed entity type
     * @param maxAirSupply a maximum air supply that entities of the constructed entity type should have
     * @param living whether the entity type should be a type of living entity
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
        return new JetEntityType(entityType.requiredFeatureFlags(), entityType.maxAirSupply(), entityType.living());
    }

    /**
     * Casts the specified {@linkplain EntityType entity type} to
     * the {@linkplain JetEntityType entity type implementation}. Throws a detailed exception
     * if the specified {@linkplain EntityType entity type} does not use the correct implementation.
     *
     * @param entityType the entity type to cast
     * @return the entity type cast to the implementation
     * @throws IllegalArgumentException if the specified entity type uses an invalid implementation
     * @since 1.0
     */
    public static @NonNull JetEntityType cast(@NonNull EntityType entityType) {
        if (!(entityType instanceof JetEntityType castEntityType))
            throw new IllegalArgumentException("The specified entity type is not a valid entity type");
        return castEntityType;
    }
}