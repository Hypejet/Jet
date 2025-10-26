package net.hypejet.jet.world.particle.simple;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;

import java.util.Objects;

/**
 * A {@linkplain Particle particle} without additional fields.
 *
 * @param particleType a holder referencing to a particle type of this particle
 * @since 1.0
 * @see ParticleType
 */
public record SimpleParticle(Holder.Reference<ParticleType> particleType) implements Particle {
    /**
     * Constructs the {@linkplain SimpleParticle simple particle}.
     *
     * @param particleType a holder referencing to the particle type of which the particle should be
     * @since 1.0
     */
    public SimpleParticle {
        Objects.requireNonNull(particleType, "particle type");
    }
}