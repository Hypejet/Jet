package net.hypejet.jet.world.particle.scalable.dust;

import net.hypejet.jet.world.particle.ParticleOptions;
import net.hypejet.jet.world.particle.scalable.ScalableParticleOptions;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * {@linkplain ScalableParticleOptions Scalable particle options} of a dust particle.
 *
 * @since 1.0
 * @see ScalableParticleOptions
 */
// TODO: Replace RGBLike with custom color record implementing RGBLike?
@ApiStatus.NonExtendable
@NullMarked
public interface DustParticleOptions extends ScalableParticleOptions {
    /**
     * Gets color of the dust particle.
     *
     * @return the dust particle color
     * @since 1.0
     */
    RGBLike color();

    /**
     * A {@linkplain ParticleOptions.Builder particle options builder}
     * of a {@linkplain DustParticleOptions dust particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @param <B> the type of this particle options builder
     * @since 1.0
     * @see DustParticleOptions
     * @see ParticleOptions.Builder
     */
    interface Builder<O extends DustParticleOptions, B extends Builder<O, B>>
            extends ScalableParticleOptions.Builder<O, B> {
        /**
         * Sets color that the dust particle should have.
         *
         * @param value the dust particle color
         * @return this particle options builder
         * @since 1.0
         */
        B color(RGBLike value);
    }
}