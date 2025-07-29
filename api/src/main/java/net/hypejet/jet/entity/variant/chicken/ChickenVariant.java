package net.hypejet.jet.entity.variant.chicken;

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
public record ChickenVariant(@NonNull ModelType modelType, @NonNull Key asset) {
    /**
     * Constructs the {@linkplain ChickenVariant chicken variant}.
     *
     * @param modelType the chicken model type to use for entities of this variant
     * @param asset the key of the texture that chickens of this variant should have
     * @since 1.0
     */
    public ChickenVariant {
        Objects.requireNonNull(modelType, "model type");
        Objects.requireNonNull(asset, "asset");
    }

    /**
     * Represents the model type of {@linkplain ChickenVariant chicken variants}.
     *
     * <p>This is not an enum since it depends on Minecraft.
     * Adding new entries could break switch cases for example.</p>
     *
     * @since 1.0
     * @see ChickenVariant
     */
    public static final class ModelType {
        /**
         * A normal mode type.
         *
         * @since 1.0
         */
        public static final ModelType NORMAL = new ModelType("normal");

        /**
         * A model type used by cold chicken variants.
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