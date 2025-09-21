package net.hypejet.jet.world.particle.sculk;

import net.hypejet.jet.world.particle.ParticleOptions;
import org.jetbrains.annotations.ApiStatus;

/**
 * {@linkplain ParticleOptions Particle options} of a sculk charge particle.
 *
 * @since 1.0
 * @see ParticleOptions
 */
@ApiStatus.NonExtendable
public interface SculkChargeParticleOptions extends ParticleOptions {
    /**
     * Gets the rotation of the sculk charge particle.
     *
     * @return the sculk charge particle rotation
     * @since 1.0
     */
    float roll();

    /**
     * A {@linkplain ParticleOptions.Builder particle options builder}
     * of a {@linkplain SculkChargeParticleOptions sculk charge particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @param <B> the type of this particle options builder
     * @since 1.0
     * @see SculkChargeParticleOptions
     * @see ParticleOptions.Builder
     */
    interface Builder<O extends SculkChargeParticleOptions, B extends Builder<O, B>>
            extends ParticleOptions.Builder<O> {
        /**
         * Sets the rotation that the sculk charge particle should have.
         *
         * @param value the rotation
         * @return this particle options builder
         * @since 1.0
         */
        B roll(float value);
    }
}