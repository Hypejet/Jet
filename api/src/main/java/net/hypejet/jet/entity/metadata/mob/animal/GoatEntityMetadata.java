package net.hypejet.jet.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.mob.ageable.AgeableMobEntityMetadata;

/**
 * A metadata of a goat {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface GoatEntityMetadata extends AgeableMobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the goat {@linkplain Entity entity} is currently screaming.
     *
     * @return {@code true} if the goat is screaming, {@code false} otherwise
     * @since 1.0
     */
    boolean screaming();

    /**
     * Gets whether the goat {@linkplain Entity entity} has a left horn.
     *
     * @return {@code true} if the goat has a left horn, {@code false} otherwise
     * @since 1.0
     */
    boolean hasLeftHorn();

    /**
     * Gets whether the goat {@linkplain Entity entity} has a right horn.
     *
     * @return {@code true} if the goat has a right horn, {@code false} otherwise
     * @since 1.0
     */
    boolean hasRightHorn();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain GoatEntityMetadata goat entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends AgeableMobEntityMetadata.Update<U> {

        /**
         * Sets whether the goat is screaming.
         *
         * @param value new screaming state
         * @return this update builder
         * @since 1.0
         */
        U screaming(boolean value);

        /**
         * Sets whether the goat has a left horn.
         *
         * @param value new left horn state
         * @return this update builder
         * @since 1.0
         */
        U leftHorn(boolean value);

        /**
         * Sets whether the goat has a right horn.
         *
         * @param value new right horn state
         * @return this update builder
         * @since 1.0
         */
        U rightHorn(boolean value);
    }
}
