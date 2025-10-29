package net.hypejet.jet.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.mob.ageable.AgeableMobEntityMetadata;

/**
 * A metadata of an ocelot {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface OcelotEntityMetadata extends AgeableMobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the ocelot {@linkplain Entity entity} is trusting.
     *
     * @return {@code true} if the ocelot is trusting, {@code false} otherwise
     * @since 1.0
     */
    boolean trusting();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain OcelotEntityMetadata ocelot entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends AgeableMobEntityMetadata.Update<U> {
        /**
         * Sets whether the ocelot is trusting.
         *
         * @param value new trusting state
         * @return this update builder
         * @since 1.0
         */
        U trusting(boolean value);
    }
}
