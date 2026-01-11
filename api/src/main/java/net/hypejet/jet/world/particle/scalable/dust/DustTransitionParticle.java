package net.hypejet.jet.world.particle.scalable.dust;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.util.color.RGBColor;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.scalable.ScalableParticle;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A color-transitioning dust {@linkplain Particle particle}.
 *
 * @param particleType a holder referencing to a particle type of this particle
 * @param scale the scale field value of this particle
 * @param fromColor the initial (before the color transition) color that this particle has
 * @param toColor the last (after the color transition) color that this particle has
 * @since 1.0
 * @see Particle
 */
@NullMarked
public record DustTransitionParticle(Holder.Reference<ParticleType> particleType,
                                     float scale, RGBColor fromColor, RGBColor toColor) implements ScalableParticle {
    /**
     * Constructs the {@linkplain DustTransitionParticle dust transition particle}.
     *
     * @param particleType a holder referencing to the particle type of which the particle should be
     * @param scale the value that the scale field of the particle should have
     * @param fromColor the initial (before the color transition) color that the particle should have
     * @param toColor the last (after the color transition) color that the particle should have
     * @since 1.0
     */
    public DustTransitionParticle {
        Objects.requireNonNull(particleType, "particle type");
        Objects.requireNonNull(fromColor, "from color");
        Objects.requireNonNull(toColor, "to color");
    }
}