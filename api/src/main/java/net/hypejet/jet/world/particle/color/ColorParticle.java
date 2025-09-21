package net.hypejet.jet.world.particle.color;

import net.hypejet.jet.util.color.ARGBColor;
import net.hypejet.jet.world.particle.Particle;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain Particle particle} containing an {@linkplain ARGBColor ARGB color}.
 *
 * @since 1.0
 * @see ARGBColor
 * @see Particle
 */
@ApiStatus.NonExtendable
@NullMarked
public interface ColorParticle extends Particle {
    /**
     * Gets the {@linkplain ARGBColor ARGB color} of this particle.
     *
     * @return the ARGB color
     * @since 1.0
     */
    ARGBColor color();

    /**
     * A {@linkplain Particle.Builder particle builder} of a {@linkplain ColorParticle color particle}.
     *
     * @param <P> the type of the particle that the builder is going to create
     * @param <B> the type of this particle builder
     * @since 1.0
     * @see ColorParticle
     * @see Particle.Builder
     */
    interface Builder<P extends ColorParticle, B extends Builder<P, B>> extends Particle.Builder<P> {
        /**
         * Sets {@linkplain ARGBColor ARGB color} that the particle should have.
         *
         * @param value the ARGB color
         * @return this particle builder
         * @since 1.0
         */
        B color(ARGBColor value);
    }
}