package net.hypejet.jet.world.particle.scalable;

import net.hypejet.jet.world.particle.ParticleOptions;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * {@linkplain ParticleOptions Particle options} containing a particle scale field.
 *
 * @since 1.0
 * @see ParticleOptions
 */
@ApiStatus.NonExtendable
@NullMarked
public interface ScalableParticleOptions extends ParticleOptions {
    /**
     * Gets scale of the particle.
     *
     * @return the particle scale
     * @since 1.0
     */
    float scale();

    /**
     * A {@linkplain ParticleOptions.Builder particle options builder}
     * of {@linkplain ScalableParticleOptions scalable particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @param <B> the type of this particle options builder
     * @since 1.0
     * @see ScalableParticleOptions
     * @see ParticleOptions.Builder
     */
    interface Builder<O extends ScalableParticleOptions, B extends Builder<O, B>> extends ParticleOptions.Builder<O> {
        /**
         * Sets scale that the particle should have.
         *
         * @param value the scale
         * @return this particle options builder
         */
        B scale(float value);
    }
}