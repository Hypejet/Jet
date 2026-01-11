package net.hypejet.jet.world.particle.sculk;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A sculk charge {@linkplain Particle particle}.
 *
 * @param particleType a holder referencing to a particle type of this particle
 * @param roll the rotation of this particle
 * @since 1.0
 * @see Particle
 */
@NullMarked
public record SculkChargeParticle(Holder.Reference<ParticleType> particleType, float roll) implements Particle {
    /**
     * Constructs the {@linkplain SculkChargeParticle sculk charge particle}.
     *
     * @param particleType a holder referencing to the particle type of which the particle should be
     * @param roll the rotation that the particle should have
     * @since 1.0
     */
    public SculkChargeParticle {
        Objects.requireNonNull(particleType, "particle type");
    }
}