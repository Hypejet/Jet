package net.hypejet.jet.world.particle.scalable.dust;

import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.scalable.ScalableParticle;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A color-transitioning dust {@linkplain Particle particle}.
 *
 * @since 1.0
 * @see Particle
 */
// TODO: Replace RGBLike with custom color record implementing RGBLike?
@ApiStatus.NonExtendable
@NullMarked
public interface DustTransitionParticle extends ScalableParticle {
    /**
     * Gets the initial (before the color transition) color that this dust particle should have.
     *
     * @return the initial color
     * @since 1.0
     */
    RGBLike fromColor();

    /**
     * Gets the last (after the color transition) color that this dust particle should have.
     *
     * @return the last color
     * @since 1.0
     */
    RGBLike toColor();

    /**
     * A {@linkplain Particle.Builder particle builder}
     * of a {@linkplain DustTransitionParticle dust transition particle}.
     *
     * @param <P> the type of the particle that the builder is going to create
     * @param <B> the type of this particle builder
     * @since 1.0
     * @see DustTransitionParticle
     * @see Particle.Builder
     */
    interface Builder<P extends DustTransitionParticle, B extends Builder<P, B>>
            extends ScalableParticle.Builder<P, B> {
        /**
         * Sets the initial (before the color transition) color that the dust particle should have.
         *
         * @param value the initial dust particle color
         * @return this builder
         * @since 1.0
         */
        B fromColor(RGBLike value);

        /**
         * Sets the last (after the color transition) color that the dust particle should have.
         *
         * @return the last color
         * @since 1.0
         */
        B toColor(RGBLike value);
    }
}