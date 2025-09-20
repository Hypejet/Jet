package net.hypejet.jet.entity.metadata.mob.creature;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.mob.MobEntityMetadata;

/**
 * A metadata of a allay {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface AllayEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the allay {@linkplain Entity entity} is dancing.
     *
     * @return {@code true} if the allay entity is dancing, {@code false} otherwise
     * @since 1.0
     */
    boolean dancing();

    /**
     * Gets whether the allay {@linkplain Entity entity} can duplicate.
     *
     * @return {@code true} if the allay entity can duplicate, {@code false} otherwise
     * @since 1.0
     */
    boolean duplicate();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain AllayEntityMetadata allay entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {        
        /**;:w
         *
         * Sets whether the allay {@linkplain Entity entity} should be dancing.
         *
         * @param value {@code true} if the allay entity should be dancing, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U dancing(boolean value);
        
        /**
         * Sets whether the allay {@linkplain Entity entity} should be able to duplicate.
         *
         * @param value {@code true} if the allay entity should be able to duplicate, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U duplicate(boolean value);
    }
}
