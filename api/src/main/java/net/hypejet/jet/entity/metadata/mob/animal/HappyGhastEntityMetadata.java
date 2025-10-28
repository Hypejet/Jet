package net.hypejet.jet.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.mob.ageable.AgeableMobEntityMetadata;

/**
 * A metadata of a happy ghast {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface HappyGhastEntityMetadata extends AgeableMobEntityMetadata {

    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the happy ghast {@linkplain Entity entity} is leash holder.
     *
     * @return {@code true} if the happy ghast is leash holder, {@code false} otherwise
     * @since 1.0
     */
    boolean leashHolder();

    /**
     * Gets whether the happy ghast {@linkplain Entity entity} stays still.
     *
     * @return {@code true} if the happy ghast stays still, {@code false} otherwise
     * @since 1.0
     */
    boolean staysStill();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain HappyGhastEntityMetadata happy ghast entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends AgeableMobEntityMetadata.Update<U> {
        /**
         * Sets whether the happy ghast is leash holder.
         *
         * @param value new leash holder state
         * @return this update builder
         * @since 1.0
         */
        U leashHolder(boolean value);

        /**
         * Sets whether the happy ghast stays still.
         *
         * @param value new stays still state
         * @return this update builder
         * @since 1.0
         */
        U staysStill(boolean value);
    }
}
