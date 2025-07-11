package net.hypejet.jet.data.json.model.variant.cow;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of Minecraft cow entity.
 *
 * @param modelType the cow model type to use for entities of this variant
 * @param asset the key of the texture that cows of this variant should have
 * @since 1.0
 */
public record JsonCowVariant(@NonNull ModelType modelType, @NonNull Key asset) {
    /**
     * Constructs the {@linkplain JsonCowVariant cow variant}.
     *
     * @param modelType the cow model type to use for entities of this variant
     * @param asset the key of the texture that cows of this variant should have
     * @since 1.0
     */
    public JsonCowVariant {
        Objects.requireNonNull(modelType, "model type");
        Objects.requireNonNull(asset, "asset");
    }

    /**
     * Represents the model type of {@linkplain JsonCowVariant cow variants}.
     *
     * @since 1.0
     * @see JsonCowVariant
     */
    public enum ModelType {
        /**
         * A normal model type.
         *
         * @since 1.0
         */
        NORMAL,
        /**
         * A model type used by cold cow variants.
         *
         * @since 1.0
         */
        COLD,
        /**
         * A model type used by warm cow variants.
         *
         * @since 1.0
         */
        WARM
    }
}