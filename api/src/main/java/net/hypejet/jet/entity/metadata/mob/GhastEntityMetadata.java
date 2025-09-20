package net.hypejet.jet.entity.metadata.mob;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;

/**
 * A metadata of a ghast {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface GhastEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the ghast {@linkplain Entity entity} is attacking.
     *
     * @return {@code true} if the ghast entity is attacking, {@code false} otherwise
     * @since 1.0
     */
    boolean attacking();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain GhastEntityMetadata ghast entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {
        
        /**
         * Sets whether the ghast {@linkplain Entity entity} should be attacking.
         *
         * @param value {@code true} if the ghast entity should be attacking, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U attacking(boolean value);
    }
}
