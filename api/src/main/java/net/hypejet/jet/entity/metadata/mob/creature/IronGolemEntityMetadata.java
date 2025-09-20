package net.hypejet.jet.entity.metadata.mob.creature;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.mob.MobEntityMetadata;

/**
 * A metadata of an iron golem {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface IronGolemEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets whether the iron golem {@linkplain Entity entity} was created by a player.
     *
     * @return {@code true} if the iron golem entity was created by a player, {@code false} otherwise
     * @since 1.0
     */
    boolean playerCreated();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain IronGolemEntityMetadata iron golem entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {        
        /**
         * Sets whether the iron golem {@linkplain Entity entity} was created by a player.
         *
         * @param value {@code true} if the iron golem entity was created by a player, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U playerCreated(boolean value); 
    }
}
