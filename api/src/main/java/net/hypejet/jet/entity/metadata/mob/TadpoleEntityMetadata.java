package net.hypejet.jet.entity.metadata.mob;

import org.jspecify.annotations.NullMarked;

/**
 * A metadata of a tadpole {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface TadpoleEntityMetadata extends CreatureEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the tadpole {@linkplain Entity entity} is from bucket.
     *
     * @return {@code true} if the tadpole entity is from bucket, {@code false} otherwise
     * @since 1.0
     */
    boolean fromBucket();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain TadpoleEntityMetadata tadpole entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {
        /**
         * Sets whether the tadpole {@linkplain Entity entity} should be from bucket.
         *
         * @param value {@code true} if the tadpole entity should is from bucket, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U fromBucket(boolean value);
    }
}
