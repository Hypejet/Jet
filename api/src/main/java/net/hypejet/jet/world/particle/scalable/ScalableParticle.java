package net.hypejet.jet.world.particle.scalable;

import net.hypejet.jet.world.particle.Particle;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain Particle particle} containing a particle scale field.
 *
 * @since 1.0
 * @see Particle
 */
@ApiStatus.NonExtendable
@NullMarked
public interface ScalableParticle extends Particle {
    /**
     * Gets scale of this particle.
     *
     * @return the particle scale
     * @since 1.0
     */
    float scale();

    /**
     * A {@linkplain Particle.Builder particle builder} of a {@linkplain ScalableParticle scalable particle}.
     *
     * @param <P> the type of the particle that the builder is going to create
     * @param <B> the type of this particle builder
     * @since 1.0
     * @see ScalableParticle
     * @see Particle.Builder
     */
    interface Builder<P extends ScalableParticle, B extends Builder<P, B>> extends Particle.Builder<P> {
        /**
         * Sets scale that the particle should have.
         *
         * @param value the scale
         * @return this particle builder
         */
        B scale(float value);
    }
}