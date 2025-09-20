package net.hypejet.jet.entity.metadata.mob.creature;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.mob.MobEntityMetadata;

/**
 * A metadata of a snow golem {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface SnowGolemEntityMetadata extends MobEntityMetadata { 
    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the snow golem {@linkplain Entity entity} has a pumpkin hat.
     *
     * @return {@code true} if the snow golem has a pumpkin hat, {@code false} otherwise
     * @since 1.0
     */
    boolean pumpkinHat();

    /**
     * An {@linkplain MobEntityMetadata.Update entity metadata update} for snow golem.
     *
     * @param <U> the type of this update
     * @since 1.0
     * @see MobEntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {
        /**
         * Sets whether the snow golem {@linkplain Entity entity} should have a pumpkin hat.
         *
         * @param value {@code true} if the snow golem should have a pumpkin hat, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U pumpkinHat(boolean value);
    }
}
