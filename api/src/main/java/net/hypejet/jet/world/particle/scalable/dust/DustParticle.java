package net.hypejet.jet.world.particle.scalable.dust;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.util.color.RGBColor;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.scalable.ScalableParticle;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A dust {@linkplain Particle particle}.
 *
 * @param particleType a holder referencing to a particle type of this particle
 * @param scale the scale field value of this particle
 * @param color the color of this particle
 * @since 1.0
 * @see Particle
 */
@NullMarked
public record DustParticle(Holder.Reference<ParticleType> particleType, float scale, RGBColor color)
        implements ScalableParticle {
    /**
     * Constructs the {@linkplain DustParticle dust particle}.
     *
     * @param particleType a holder referencing to the particle type of which the particle should be
     * @param scale the value that scale field of the particle should have
     * @param color the color that the particle should have
     * @since 1.0
     */
    public DustParticle {
        Objects.requireNonNull(particleType, "particle type");
        Objects.requireNonNull(color, "color");
    }
}