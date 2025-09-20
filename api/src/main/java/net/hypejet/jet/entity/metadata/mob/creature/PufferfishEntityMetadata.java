package net.hypejet.jet.entity.metadata.mob.creature;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.mob.MobEntityMetadata;
import net.hypejet.jet.entity.pufferfish.PufferfishState;

/**
 * A metadata of a pufferfish {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface PufferfishEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the pufferfish {@linkplain Entity entity} is from bucket.
     *
     * @return {@code true} if the pufferfish entity is from bucket, {@code false} otherwise
     * @since 1.0
     */
    boolean fromBucket();
   
    /**
     * Gets the current state of the pufferfish {@linkplain Entity entity}.
     *
     * @return the current {@link PufferfishState} of the entity
     * @since 1.0
     */
    PufferfishState state();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain PufferfishEntityMetadata pufferfish entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {
        /**
         * Sets whether the pufferfish {@linkplain Entity entity} should be from bucket.
         *
         * @param value {@code true} if the pufferfish entity should is from bucket, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U fromBucket(boolean value);
        
        /**
         * Sets the current state of the pufferfish {@linkplain Entity entity}.
         *
         * @param value the new {@link PufferfishState} to set
         * @return this update builder
         * @since 1.0
         */
        U state(PufferfishState value);
    }
}
