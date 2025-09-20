package net.hypejet.jet.entity.metadata.mob;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.enderdragon.EnderDragonPhase;
import net.hypejet.jet.entity.metadata.EntityMetadata;

/**
 * Metadata of an ender dragon {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface EnderDragonEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets the current phase of the ender dragon {@linkplain Entity entity}.
     *
     * @return the current {@link EnderDragonPhase} of the entity
     * @since 1.0
     */
    EnderDragonPhase phase();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain EnderDragonEntityMetadata ender dragon entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {
        /**
         * Sets the current phase of the ender dragon {@linkplain Entity entity}.
         *
         * @param value the new {@link EnderDragonPhase} to set
         * @return this update builder
         * @since 1.0
         */
        U phase(EnderDragonPhase value);
    }
}
