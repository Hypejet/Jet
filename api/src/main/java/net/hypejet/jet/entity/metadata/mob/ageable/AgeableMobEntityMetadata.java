package net.hypejet.jet.entity.metadata.mob.ageable;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.mob.MobEntityMetadata;

/**
 * A metadata of a ageable mob {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface AgeableMobEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the {@linkplain Entity entity} is a baby.
     *
     * @return {@code true} if the entity is a baby, {@code false} if it is an adult
     * @since 1.0
     */
    boolean baby();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain AgeableMobEntityMetadata ageable mob entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {
        /**
         * Sets whether the {@linkplain Entity entity} is a baby.
         *
         * @param value {@code true} if the entity should be a baby, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U baby(boolean value);
    }
}
