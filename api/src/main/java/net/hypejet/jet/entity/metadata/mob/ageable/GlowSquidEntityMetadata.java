package net.hypejet.jet.entity.metadata.mob.ageable;

import org.jspecify.annotations.NullMarked;
import net.hypejet.jet.entity.metadata.EntityMetadata;

/**
 * A metadata of a glow squid {@linkplain Entity entity}.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public interface GlowSquidEntityMetadata extends AgeableMobEntityMetadata {
    /**
     * {@inheritDoc}
     */
    @Override
    Update<?> createUpdateBuilder();

    /**
     * Gets the dark ticks remaining of the glow squid {@linkplain Entity entity}.
     *
     * @return dark ticks remaning
     * @since 1.0
     */ 
    int darkTicksRemaining();
    
    /**
     * An {@linkplain EntityMetadata.Update entity metadata update}
     * of {@linkplain GlowSquidEntityMetadata glow squid entity metadata}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata
     * @see EntityMetadata.Update
     */
    interface Update<U extends Update<U>> extends AgeableMobEntityMetadata.Update<U> { 
        /**
         * Sets the dark ticks remaining of the glow squid.
         *
         * @param value the new moisture level
         * @return this update builder
         * @since 1.0
         */ 
        U darkTicksRemaining(int value);   
    } 
}
