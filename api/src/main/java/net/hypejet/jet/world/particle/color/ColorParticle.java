package net.hypejet.jet.world.particle.color;

import net.hypejet.jet.world.particle.Particle;
import net.kyori.adventure.util.ARGBLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain Particle particle} containing an {@linkplain ARGBLike ARGB-like color}.
 *
 * @since 1.0
 * @see ARGBLike
 * @see Particle
 */
// TODO: Replace RGBLike with custom color record implementing ARGBLike?
@ApiStatus.NonExtendable
@NullMarked
public interface ColorParticle extends Particle {
    /**
     * Gets the {@linkplain ARGBLike ARGB-like color} of this particle.
     *
     * @return the ARGB-like color
     * @since 1.0
     */
    ARGBLike color();

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
         * Sets {@linkplain ARGBLike ARGB-like color} that the particle should have.
         *
         * @param value the ARGB-like color
         * @return this particle builder
         * @since 1.0
         */
        B color(ARGBLike value);
    }
}