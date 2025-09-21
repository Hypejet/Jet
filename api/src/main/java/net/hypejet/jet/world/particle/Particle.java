package net.hypejet.jet.world.particle;

import net.hypejet.jet.registry.holder.Holder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Options of a Minecraft particle.
 *
 * @since 1.0
 */
@ApiStatus.NonExtendable
@NullMarked
public interface Particle {
    /**
     * Gets a {@linkplain Holder.Reference holder referencing}
     * to a {@linkplain ParticleType particle type} of this {@linkplain Particle particle}.
     *
     * @return the holder referencing to the particle type
     * @since 1.0
     */
    Holder.Reference<ParticleType> particleType();

    /**
     * A builder of a {@linkplain Particle particle}.
     *
     * @param <P> the type of the particle that this builder is going to create
     * @since 1.0
     * @see Particle
     */
    interface Builder<P extends Particle> {
        /**
         * Builds the {@linkplain Particle particle}.
         *
         * @return the created particle
         * @since 1.0
         */
        P build();
    }
}