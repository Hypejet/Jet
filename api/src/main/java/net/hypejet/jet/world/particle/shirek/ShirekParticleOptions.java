package net.hypejet.jet.world.particle.shirek;

import net.hypejet.jet.world.particle.ParticleOptions;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * {@linkplain ParticleOptions Particle options} of a shirek particle.
 *
 * @since 1.0
 * @see ParticleOptions
 */
@ApiStatus.NonExtendable
@NullMarked
public interface ShirekParticleOptions extends ParticleOptions {
    /**
     * Gets the time after the particle is being actually displayed after spawning.
     *
     * @return the delay, in ticks
     * @since 1.0
     */
    int delay();

    /**
     * A {@linkplain ParticleOptions.Builder particle options builder}
     * of a {@linkplain ShirekParticleOptions shirek particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @param <B> the type of this particle options builder
     * @since 1.0
     * @see ShirekParticleOptions
     * @see ParticleOptions.Builder
     */
    interface Builder<O extends ShirekParticleOptions, B extends Builder<O, B>> extends ParticleOptions.Builder<O> {
        /**
         * Sets the time after the particle should be actually displayed after spawning.
         *
         * @param value the delay, in ticks
         * @return this particle options builder
         * @since 1.0
         */
        B delay(int value);
    }
}