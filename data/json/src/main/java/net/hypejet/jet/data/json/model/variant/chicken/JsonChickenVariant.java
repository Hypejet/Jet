package net.hypejet.jet.data.json.model.variant.chicken;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of Minecraft chicken entity.
 *
 * @param modelType the chicken model type to use for entities of this variant
 * @param asset the key of the texture that chickens of this variant should have
 * @since 1.0
 */
public record JsonChickenVariant(@NonNull ModelType modelType, @NonNull Key asset) {
    /**
     * Constructs the {@linkplain JsonChickenVariant chicken variant}.
     *
     * @param modelType the chicken model type to use for entities of this variant
     * @param asset the key of the texture that chickens of this variant should have
     * @since 1.0
     */
    public JsonChickenVariant {
        Objects.requireNonNull(modelType, "model type");
        Objects.requireNonNull(asset, "asset");
    }

    /**
     * Represents the model type of {@linkplain JsonChickenVariant chicken variants}.
     *
     * @since 1.0
     * @see JsonChickenVariant
     */
    public enum ModelType {
        /**
         * A normal mode type.
         *
         * @since 1.0
         */
        NORMAL,
        /**
         * A model type used by cold chicken variants.
         *
         * @since 1.0
         */
        COLD
    }
}