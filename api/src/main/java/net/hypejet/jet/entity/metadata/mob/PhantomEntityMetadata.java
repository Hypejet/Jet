package net.hypejet.jet.entity.metadata.mob;

import org.jspecify.annotations.NullMarked;

/**
 * A metadata of a phantom {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface PhantomEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets the size of the phantom {@linkplain Entity entity}.
     *
     * @return the size of the phantom
     * @since 1.0
     */
    int size();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain PhantomEntityMetadata phantom entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {
        /**
         * Sets the size of the phantom {@linkplain Entity entity}.
         *
         * @param value the size of the phantom
         * @return this update builder
         * @since 1.0
         */
        U size(int value);
    }
}
