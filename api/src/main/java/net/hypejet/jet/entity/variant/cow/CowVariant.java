package net.hypejet.jet.entity.variant.cow;

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
public record CowVariant(@NonNull ModelType modelType, @NonNull Key asset) {
    /**
     * Constructs the {@linkplain CowVariant cow variant}.
     *
     * @param modelType the cow model type to use for entities of this variant
     * @param asset the key of the texture that cows of this variant should have
     * @since 1.0
     */
    public CowVariant {
        Objects.requireNonNull(modelType, "model type");
        Objects.requireNonNull(asset, "asset");
    }

    /**
     * The model type of {@linkplain CowVariant cow variants}.
     *
     * <p>This is not an enum since it depends on Minecraft.
     * Adding new entries could break switch cases for example.</p>
     *
     * @since 1.0
     * @see CowVariant
     */
    public static final class ModelType {
        /**
         * A normal model type.
         *
         * @since 1.0
         */
        public static final ModelType NORMAL = new ModelType("normal");

        /**
         * A model type used by cold cow variants.
         *
         * @since 1.0
         */
        public static final ModelType COLD = new ModelType("cold");

        /**
         * A model type used by warm cow variants.
         *
         * @since 1.0
         */
        public static final ModelType WARM = new ModelType("warm");

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