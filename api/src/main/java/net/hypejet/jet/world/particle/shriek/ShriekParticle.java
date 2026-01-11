package net.hypejet.jet.world.particle.shriek;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A shriek {@linkplain Particle particle}.
 *
 * @param particleType a holder referencing to a particle type of this particle
 * @param delay the time after which this particle is actually displayed after spawning, in ticks
 * @since 1.0
 * @see Particle
 */
@NullMarked
public record ShriekParticle(Holder.Reference<ParticleType> particleType, int delay) implements Particle {
    /**
     * Constructs the {@linkplain ShriekParticle shriek particle}.
     *
     * @param particleType a holder referencing to the particle type of which the particle should be
     * @param delay the time after which the particle should be actually displayed after spawning, in ticks
     * @since 1.0
     */
    public ShriekParticle {
        Objects.requireNonNull(particleType, "particle type");
    }
}