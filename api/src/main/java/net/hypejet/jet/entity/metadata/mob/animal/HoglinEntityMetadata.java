package net.hypejet.jet.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.mob.ageable.AgeableMobEntityMetadata;

/**
 * A metadata of a hoglin {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface HoglinEntityMetadata extends AgeableMobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the hoglin {@linkplain Entity entity} is immune to zombification.
     *
     * @return {@code true} if the hoglin is immune to zombification, {@code false} otherwise
     * @since 1.0
     */
    boolean immuneToZombification();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain HoglinEntityMetadata hoglin entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends AgeableMobEntityMetadata.Update<U> {
        /**
         * Sets whether the hoglin is immune to zombification.
         *
         * @param value new immune state
         * @return this update builder
         * @since 1.0
         */
        U immuneToZombification(boolean value);
    }
}
