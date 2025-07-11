package net.hypejet.jet.data.json.model.variant.pig;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of Minecraft pig entity.
 *
 * @param modelType the pig model type to use for entities of this variant
 * @param asset the key of the texture that pigs of this variant should have
 * @since 1.0
 */
public record JsonPigVariant(@NonNull ModelType modelType, @NonNull Key asset) {
    /**
     * Constructs the {@linkplain JsonPigVariant pig variant}.
     *
     * @param modelType the pig model type to use for entities of this variant
     * @param asset the key of the texture that pigs of this variant should have
     * @since 1.0
     */
    public JsonPigVariant {
        Objects.requireNonNull(modelType, "model type");
        Objects.requireNonNull(asset, "asset");
    }

    /**
     * Represents the model type of {@linkplain JsonPigVariant pig variants}.
     *
     * @since 1.0
     * @see JsonPigVariant
     */
    public enum ModelType {
        /**
         * A normal model type.
         *
         * @since 1.0
         */
        NORMAL,
        /**
         * A model type used by cold pig variants.
         *
         * @since 1.0
         */
        COLD
    }
}