package net.hypejet.jet.world.particle;

import org.jetbrains.annotations.ApiStatus;

/**
 * An options of a Minecraft particle.
 *
 * @since 1.0
 */
@ApiStatus.NonExtendable
public interface ParticleOptions {
    /**
     * A builder of {@linkplain ParticleOptions particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @since 1.0
     * @see ParticleOptions
     */
    interface Builder<O extends ParticleOptions> {
        /**
         * Builds the {@linkplain ParticleOptions particle options}.
         *
         * @return the created particle options
         * @since 1.0
         */
        O build();
    }
}