package net.hypejet.jet.world.particle;

import net.hypejet.jet.registry.holder.Holder;
import org.jspecify.annotations.NullMarked;

/**
 * Something managing creation of {@linkplain Particle particles}.
 *
 * @since 1.0
 * @see Particle
 */
@NullMarked
public interface ParticleManager {
    /**
     * Creates a {@linkplain Particle.Builder particle builder}
     * for the specified {@linkplain ParticleType particle type}.
     *
     * @param particleType a holder referencing to a particle type for which the particle builder should be created
     * @return the created particle builder
     * @since 1.0
     */
    Particle.Builder<?> createParticleBuilder(Holder.Reference<ParticleType> particleType);
}