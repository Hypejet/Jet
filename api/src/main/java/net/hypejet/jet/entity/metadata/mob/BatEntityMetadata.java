package net.hypejet.jet.entity.metadata.mob;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import org.jspecify.annotations.NullMarked;

/**
 * A metadata of a bat {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface BatEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the bat {@linkplain Entity entity} is resting.
     *
     * @return {@code true} if the bat entity is resting, {@code false} otherwise
     * @since 1.0
     */
    boolean resting();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain BatEntityMetadata bat entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {
        /**
         * Sets whether the bat {@linkplain Entity entity} should be resting.
         *
         * @param value {@code true} if the bat entity should be resting, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U resting(boolean value);
    }
}