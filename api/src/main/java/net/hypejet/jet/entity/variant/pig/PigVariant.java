package net.hypejet.jet.entity.variant.pig;

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
public record PigVariant(@NonNull ModelType modelType, @NonNull Key asset) {
    /**
     * Constructs the {@linkplain PigVariant pig variant}.
     *
     * @param modelType the pig model type to use for entities of this variant
     * @param asset the key of the texture that pigs of this variant should have
     * @since 1.0
     */
    public PigVariant {
        Objects.requireNonNull(modelType, "model type");
        Objects.requireNonNull(asset, "asset");
    }

    /**
     * Represents the model type of {@linkplain PigVariant pig variants}.
     *
     * <p>This is not an enum since it depends on Minecraft.
     * Adding new entries could break switch cases for example.</p>
     *
     * @since 1.0
     * @see PigVariant
     */
    public static final class ModelType {
        /**
         * A normal model type.
         *
         * @since 1.0
         */
        public static final ModelType NORMAL = new ModelType("normal");

        /**
         * A model type used by cold pig variants.
         *
         * @since 1.0
         */
        public static final ModelType COLD = new ModelType("cold");

        private final String name;

        private ModelType(@NonNull String name) {
            this.name = Objects.requireNonNull(name, "name");
        }

        @Override
        public String toString() {
            return "ModelType{" +
                    "name='" + this.name + '\'' +
                    '}';
        }
    }
}