package net.hypejet.jet.server.world.particle;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of {@linkplain Particle particle}.
 *
 * @since 1.0
 * @see Particle
 */
@NullMarked
public class JetParticle implements Particle {

    private final Holder.Reference<ParticleType> particleType;

    /**
     * Constructs the {@linkplain JetParticle particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @since 1.0
     */
    protected JetParticle(Holder.Reference<ParticleType> particleType) {
        this.particleType = Objects.requireNonNull(particleType, "particle type");
    }

    @Override
    public final Holder.Reference<ParticleType> particleType() {
        return this.particleType;
    }

    /**
     * Casts the specified {@linkplain Particle particle} to the {@linkplain JetParticle particle implementation}.
     * Throws a detailed exception if the specified {@linkplain Particle particle} does not use
     * the correct implementation.
     *
     * @param particle the particle to cast
     * @return the particle cast to the implementation
     * @throws IllegalArgumentException if the specified particle uses an invalid implementation
     * @since 1.0
     */
    public static JetParticle cast(Particle particle) {
        if (!(particle instanceof JetParticle castParticle))
            throw new IllegalArgumentException("The specified particle is not a valid particle");
        return castParticle;
    }

    /**
     * An implementation of the {@linkplain Particle.Builder particle builder}
     * creating {@linkplain Particle particles} without additional options.
     *
     * @see Particle.Builder
     * @since 1.0
     */
    public record Builder(Holder.Reference<ParticleType> particleType) implements Particle.Builder<JetParticle> {
        /**
         * Constructs the {@linkplain Builder particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @since 1.0
         */
        public Builder {
            Objects.requireNonNull(particleType, "particle type");
        }

        @Override
        public JetParticle build() {
            return new JetParticle(this.particleType);
        }
    }
}