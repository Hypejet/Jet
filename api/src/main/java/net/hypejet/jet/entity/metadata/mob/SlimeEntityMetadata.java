package net.hypejet.jet.entity.metadata.mob;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;

/**
 * A metadata of a slime {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface SlimeEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets the size of the slime {@linkplain Entity entity}.
     *
     * @return the size of the slime
     * @since 1.0
     */
    int size();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain SlimeEntityMetadata slime entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {
        /**
         * Sets the size of the slime {@linkplain Entity entity}.
         *
         * @param value the size of the slime
         * @return this update builder
         * @since 1.0
         */
        U size(int value);
    }
}
