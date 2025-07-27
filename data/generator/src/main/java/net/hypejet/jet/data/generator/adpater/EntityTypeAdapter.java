package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain EntityType entity types} to a Jet data equivalent.
 *
 * @since 1.0
 * @see EntityType
 */
public final class EntityTypeAdapter {

    private EntityTypeAdapter() {}

    /**
     * Converts the specified {@linkplain EntityType entity type} to a Jet data equivalent.
     *
     * @param type the entity type to convert
     * @return the converted entity type
     * @since 1.0
     */
    public static @NonNull JsonEntityType convert(@NonNull EntityType<?> type) {
        return new JsonEntityType(KeyAdapter.convertSet(FeatureFlags.REGISTRY.toNames(type.requiredFeatures())));
    }
}