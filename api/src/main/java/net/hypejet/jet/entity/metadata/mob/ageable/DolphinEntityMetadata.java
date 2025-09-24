package net.hypejet.jet.entity.metadata.mob.ageable;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.mob.MobEntityMetadata;

/**
 * A metadata of a dolphin {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface DolphinEntityMetadata extends MobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();
    
    /**
     * Gets whether the dolphin is holding a fish.
     *
     * @return {@code true} if the dolphin has a fish, {@code false} otherwise
     * @since 1.0
     */
    boolean fish();

    /**
     * Gets the moisture level of the dolphin {@linkplain Entity entity}.
     *
     * @return moisture level
     * @since 1.0
     */
    int moistureLevel();

    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain DolphinEntityMetadata dolphin entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends MobEntityMetadata.Update<U> {        
        /**
         * Sets whether the dolphin {@linkplain Entity entity} should be holding a fish.
         *
         * @param value {@code true} if the dolphin should have a fish, {@code false} otherwise
         * @return this update builder
         * @since 1.0
         */
        U fish(boolean value);
  
        /**
         * Sets the moisture level of the dolphin.
         *
         * @param value the new moisture level
         * @return this update builder
         * @since 1.0
         */
        U moistureLevel(int value);
    }
}
