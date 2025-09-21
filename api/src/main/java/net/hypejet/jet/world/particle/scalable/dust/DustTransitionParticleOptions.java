package net.hypejet.jet.world.particle.scalable.dust;

import net.hypejet.jet.world.particle.ParticleOptions;
import net.hypejet.jet.world.particle.scalable.ScalableParticleOptions;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
/**
 * {@linkplain ScalableParticleOptions Scalable particle options} of a dust particle with transitioning color.
 *
 * @since 1.0
 * @see ScalableParticleOptions
 */
// TODO: Replace RGBLike with custom color record implementing RGBLike?
@ApiStatus.NonExtendable
@NullMarked
public interface DustTransitionParticleOptions extends ScalableParticleOptions {
    /**
     * Gets the initial (before the color transition) color that the dust particle should have.
     *
     * @return the initial color
     * @since 1.0
     */
    RGBLike fromColor();

    /**
     * Gets the last (after the color transition) color that the dust particle should have.
     *
     * @return the last color
     * @since 1.0
     */
    RGBLike toColor();

    /**
     * A {@linkplain ParticleOptions.Builder particle options builder}
     * of a {@linkplain DustTransitionParticleOptions dust transition particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @param <B> the type of this particle options builder
     * @since 1.0
     * @see DustTransitionParticleOptions
     * @see ParticleOptions.Builder
     */
    interface Builder<O extends DustTransitionParticleOptions, B extends Builder<O, B>>
            extends ScalableParticleOptions.Builder<O, B> {
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