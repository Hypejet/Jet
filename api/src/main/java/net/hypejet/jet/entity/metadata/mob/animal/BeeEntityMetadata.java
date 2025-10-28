package net.hypejet.jet.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.mob.ageable.AgeableMobEntityMetadata;

/**
 * A metadata of a bee {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface BeeEntityMetadata extends AgeableMobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the bee {@linkplain Entity entity} is currently angry.
     *
     * @return {@code true} if the bee is angry, {@code false} otherwise
     * @since 1.0
     */
    boolean angry();

    /**
     * Gets whether the bee {@linkplain Entity entity} has stung.
     *
     * @return {@code true} if the bee has stung, {@code false} otherwise
     * @since 1.0
     */
    boolean stung();

    /**
     * Gets whether the bee {@linkplain Entity entity} has nectar.
     *
     * @return {@code true} if the bee has nectar, {@code false} otherwise
     * @since 1.0
     */
    boolean nectar();

    /**
     * Gets the anger time of the bee {@linkplain Entity entity}.
     *
     * @return anger time in ticks
     * @since 1.0
     */
    int angerTimeTicks();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain BeeEntityMetadata bee entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends AgeableMobEntityMetadata.Update<U> {
        /**
         * Sets whether the bee is angry.
         *
         * @param value new angry state
         * @return this update builder
         * @since 1.0
         */
        U angry(boolean value);

        /**
         * Sets whether the bee has stung.
         *
         * @param value new stung state
         * @return this update builder
         * @since 1.0
         */
        U stung(boolean value);

        /**
         * Sets whether the bee has nectar.
         *
         * @param value new nectar state
         * @return this update builder
         * @since 1.0
         */
        U nectar(boolean value);

        /**
         * Sets the anger time of the bee.
         *
         * @param value anger time in ticks
         * @return this update builder
         * @since 1.0
         */
        U angerTimeTicks(int value);
    }
}
