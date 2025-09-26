package net.hypejet.jet.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.armadillo.ArmadilloState;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.metadata.mob.ageable.AgeableMobEntityMetadata;

/**
 * A metadata of a armadillo {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface ArmadilloEntityMetadata extends AgeableMobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets the state of the armadillo {@linkplain Entity entity}.
     *
     * @return armadillo state
     * @since 1.0
     */ 
    ArmadilloState state();
    
    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain ArmadilloEntityMetadata armadillo entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends AgeableMobEntityMetadata.Update<U> { 
        /**
         * Sets the state of the armadillo.
         *
         * @param value the new state of the armadillo
         * @return this update builder
         * @since 1.0
         */
        U state(ArmadilloState value);   
    } 
}
