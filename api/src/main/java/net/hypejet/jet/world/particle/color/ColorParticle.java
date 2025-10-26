package net.hypejet.jet.world.particle.color;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.util.color.ARGBColor;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A {@linkplain Particle particle} containing an {@linkplain ARGBColor ARGB color} field.
 *
 * @param particleType a holder referencing to a particle type of this particle
 * @param color the ARGB color field value of this particle
 * @since 1.0
 * @see ARGBColor
 * @see Particle
 */
@NullMarked
public record ColorParticle(Holder.Reference<ParticleType> particleType, ARGBColor color) implements Particle {
    /**
     * Constructs the {@linkplain ColorParticle color particle}.
     *
     * @param particleType a holder referencing to the particle type of which the particle should be
     * @param color the value that the ARGB color field of the particle should have
     * @since 1.0
     */
    public ColorParticle {
        Objects.requireNonNull(particleType, "particle type");
        Objects.requireNonNull(color, "color");
    }
}