package net.hypejet.jet.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.mob.ageable.AgeableMobEntityMetadata;
import net.hypejet.jet.entity.mooshroom.MooshRoomVariant;

/**
 * A metadata of a mooshroom {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface MooshRoomEntityMetadata extends AgeableMobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets the variant of the mooshroom {@linkplain Entity entity}.
     *
     * @return the mooshroom variant
     * @since 1.0
     */
    MooshRoomVariant variant();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain MooshRoomEntityMetadata mooshroom entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends AgeableMobEntityMetadata.Update<U> {
        /**
         * Sets the variant of the mooshroom.
         *
         * @param value new mooshroom variant
         * @return this update builder
         * @since 1.0
         */
        U variant(MooshRoomVariant value);
    }
}
