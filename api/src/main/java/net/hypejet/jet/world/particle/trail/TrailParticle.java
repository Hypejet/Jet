package net.hypejet.jet.world.particle.trail;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.util.color.RGBColor;
import net.hypejet.jet.world.coordinate.Vector;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A trial {@linkplain Particle particle}.
 *
 * @param particleType a holder referencing to a particle type of this particle
 * @param target the destination that this particle travels to after spawning, as a vector
 * @param color the color of this particle
 * @param travelDuration the time that it takes for this particle to travel from
 *                       the starting position to the destination, in ticks
 * @since 1.0
 * @see Particle
 */
@NullMarked
public record TrailParticle(Holder.Reference<ParticleType> particleType, Vector target,
                            RGBColor color, int travelDuration) implements Particle {
    /**
     * Constructs the {@linkplain TrailParticle trail particle}.
     *
     * @param particleType a holder referencing to the particle type of which the particle should be
     * @param target the destination that the particle should start travelling to after spawning, as a vector
     * @param color the color that the particle should have
     * @param travelDuration the time that it should take for the particle to travel
     *                       from the starting position to destination, in ticks
     * @since 1.0
     */
    public TrailParticle {
        Objects.requireNonNull(particleType, "particle type");
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(color, "color");
    }
}