package net.hypejet.jet.world.particle.scalable.dust;

import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.scalable.ScalableParticle;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A dust {@linkplain Particle particle}.
 *
 * @since 1.0
 * @see Particle
 */
// TODO: Replace RGBLike with custom color record implementing RGBLike?
@ApiStatus.NonExtendable
@NullMarked
public interface DustParticle extends ScalableParticle {
    /**
     * Gets color of this dust particle.
     *
     * @return the dust particle color
     * @since 1.0
     */
    RGBLike color();

    /**
     * A {@linkplain Particle.Builder particle builder} of a {@linkplain DustParticle dust particle}.
     *
     * @param <P> the type of the particle that the builder is going to create
     * @param <B> the type of this particle builder
     * @since 1.0
     * @see DustParticle
     * @see Particle.Builder
     */
    interface Builder<P extends DustParticle, B extends Builder<P, B>>
            extends ScalableParticle.Builder<P, B> {
        /**
         * Sets color that the dust particle should have.
         *
         * @param value the dust particle color
         * @return this particle builder
         * @since 1.0
         */
        B color(RGBLike value);
    }
}