package net.hypejet.jet.world.particle.shriek;

import net.hypejet.jet.world.particle.Particle;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A shriek {@linkplain Particle particle}.
 *
 * @since 1.0
 * @see Particle
 */
@ApiStatus.NonExtendable
@NullMarked
public interface ShriekParticle extends Particle {
    /**
     * Gets the time after which the particle is actually being displayed after spawning.
     *
     * @return the delay, in ticks
     * @since 1.0
     */
    int delay();

    /**
     * A {@linkplain Particle.Builder particle builder} of a {@linkplain ShriekParticle shriek particle}.
     *
     * @param <P> the type of the particle that the builder is going to create
     * @param <B> the type of this particle builder
     * @since 1.0
     * @see ShriekParticle
     * @see Particle.Builder
     */
    interface Builder<P extends ShriekParticle, B extends Builder<P, B>> extends Particle.Builder<P> {
        /**
         * Sets the time after which the particle should be actually displayed after spawning.
         *
         * @param value the delay, in ticks
         * @return this particle builder
         * @since 1.0
         */
        B delay(int value);
    }
}