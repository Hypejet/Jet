package net.hypejet.jet.world.particle.scalable;

import net.hypejet.jet.world.particle.Particle;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain Particle particle} containing a particle scale field.
 *
 * @since 1.0
 * @see Particle
 */
@ApiStatus.NonExtendable
@NullMarked
public interface ScalableParticle extends Particle {
    /**
     * Gets the scale field value of this particle.
     *
     * @return the scale field value
     * @since 1.0
     */
    float scale();
}