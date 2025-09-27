package net.hypejet.jet.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.axolotl.AxolotlVariant;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.mob.ageable.AgeableMobEntityMetadata;

/**
 * A metadata of a axolotl {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface AxolotlEntityMetadata extends AgeableMobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets the variant of the axolotl {@linkplain Entity entity}.
     *
     * @return axolotl variant
     * @since 1.0
     */
    AxolotlVariant variant();

    /**
     * Gets whether the axolotl {@linkplain Entity entity} is currently playing dead.
     *
     * @return {@code true} if the axolotl is playing dead, {@code false} otherwise
     * @since 1.0
     */
    boolean playingDead();

    /**
     * Gets whether the axolotl {@linkplain Entity entity} is from bucket.
     *
     * @return {@code true} if the axolotl entity is from bucket, {@code false} otherwise
     * @since 1.0
     */
    boolean fromBucket();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain AxolotlEntityMetadata axolotl entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends AgeableMobEntityMetadata.Update<U> {
        /**
         * Sets the variant of the axolotl.
         *
         * @param value the new variant of the axolotl
         * @return this update builder
         * @since 1.0
         */
        U variant(AxolotlVariant value);

        /**
         * Sets whether the axolotl is playing dead.
         *
         * @param value new playing-dead state
         * @return this update builder
         * @since 1.0
         */
        U playingDead(boolean value);

        /**
         * Sets whether the axolotl was spawned from a bucket.
         *
         * @param value new from-bucket state
         * @return this update builder
         * @since 1.0
         */
        U fromBucket(boolean value);
    }
}
