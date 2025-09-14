package net.hypejet.jet.entity.metadata.mob;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.LivingEntityMetadata;
import org.jspecify.annotations.NullMarked;

/**
 * A metadata of a mob {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface MobEntityMetadata extends LivingEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the {@linkplain Entity entity} has no AI.
     *
     * @return {@code true} if the entity has no AI, {@code false} otherwise
     * @since 1.0
     */
    boolean noAi();

    /**
     * Gets whether the {@linkplain Entity entity} is left-handed.
     *
     * @return {@code true} if the entity is left-handed, {@code false} otherwise
     * @since 1.0
     */
    boolean leftHanded();

    /**
     * Gets whether the {@linkplain Entity entity} is aggressive.
     *
     * @return {@code true} if the entity is aggressive, {@code false} otherwise
     * @since 1.0
     */
    boolean aggressive();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain MobEntityMetadata mob entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends LivingEntityMetadata.Update<U> {
        /**
         * Sets whether the {@linkplain Entity entity} should have no AI.
         *
         * @param value {@code true} if the entity should have no AI, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U noAi(boolean value);

        /**
         * Sets whether the {@linkplain Entity entity} should be left-handed.
         *
         * @param value {@code true} if the entity should be left-handed, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U leftHanded(boolean value);

        /**
         * Sets whether the {@linkplain Entity entity} should be aggressive.
         *
         * @param value {@code true} if the entity should be aggressive, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U aggressive(boolean value);
    }
}