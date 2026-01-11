package net.hypejet.jet.world.particle.vibration;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A vibration {@linkplain Particle particle}.
 *
 * @param particleType a holder referencing to a particle type of this particle
 * @param destination a block position of the destination that this particle travels to after spawning
 * @param arrivalDuration the time that it takes for this particle to travel from
 *                        the starting position to the specified destination, in ticks
 * @since 1.0
 * @see Particle
 */
@NullMarked
public record VibrationParticle(Holder.Reference<ParticleType> particleType,
                                BlockPosition destination, int arrivalDuration) implements Particle {
    /**
     * Constructs the {@linkplain VibrationParticle vibration particle}.
     *
     * @param particleType a holder referencing to the particle type of which the particle should be
     * @param destination a block position of the destination that the particle
     *                    should start travelling to after spawning
     * @param arrivalDuration the time that it should take for the particle to travel
     *                        from the starting position to the specified destination
     * @since 1.0
     */
    public VibrationParticle {
        Objects.requireNonNull(particleType, "particle type");
        Objects.requireNonNull(destination, "destination");
    }
}