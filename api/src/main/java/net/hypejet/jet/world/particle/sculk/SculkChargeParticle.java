package net.hypejet.jet.world.particle.sculk;

import net.hypejet.jet.world.particle.Particle;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A sculk charge {@linkplain Particle particle}.
 *
 * @since 1.0
 * @see Particle
 */
@ApiStatus.NonExtendable
@NullMarked
public interface SculkChargeParticle extends Particle {
    /**
     * Gets the rotation of this sculk charge particle.
     *
     * @return the sculk charge particle rotation
     * @since 1.0
     */
    float roll();

    /**
     * A {@linkplain Particle.Builder particle builder}
     * of a {@linkplain SculkChargeParticle sculk charge particle}.
     *
     * @param <P> the type of the particle that the builder is going to create
     * @param <B> the type of this particle builder
     * @since 1.0
     * @see SculkChargeParticle
     * @see Particle.Builder
     */
    interface Builder<P extends SculkChargeParticle, B extends Builder<P, B>>
            extends Particle.Builder<P> {
        /**
         * Sets the rotation that the sculk charge particle should have.
         *
         * @param value the rotation
         * @return this particle builder
         * @since 1.0
         */
        B roll(float value);
    }
}