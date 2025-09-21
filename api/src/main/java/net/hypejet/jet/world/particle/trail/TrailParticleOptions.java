package net.hypejet.jet.world.particle.trail;

import net.hypejet.jet.world.coordinate.Vector;
import net.hypejet.jet.world.particle.ParticleOptions;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * {@linkplain ParticleOptions Particle options} of the trail particle.
 *
 * @since 1.0
 * @see ParticleOptions
 */
// TODO: Replace RGBLike with custom color record implementing RGBLike?
@ApiStatus.NonExtendable
@NullMarked
public interface TrailParticleOptions extends ParticleOptions {
    /**
     * Gets the destination that the trail particle should start travelling to after spawning.
     *
     * @return the destination, as vector
     * @since 1.0
     */
    Vector target();

    /**
     * Gets the color of the trail particle.
     *
     * @return the trail particle color
     * @since 1.0
     */
    RGBLike color();

    /**
     * Gets the time that it takes for the particle to travel
     * from the starting position to the destination.
     *
     * @return the particle travel time, in ticks
     * @since 1.0
     */
    int duration();

    /**
     * A {@linkplain ParticleOptions.Builder particle options builder}
     * of {@linkplain TrailParticleOptions trail particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @param <B> the type of this particle options builder
     * @since 1.0
     * @see TrailParticleOptions
     * @see ParticleOptions.Builder
     */
    interface Builder<O extends TrailParticleOptions, B extends Builder<O, B>> extends ParticleOptions.Builder<O> {
        /**
         * Sets the destination that the trail particle should start travelling to after spawning.
         *
         * @param value the destination, as vector
         * @return this particle options builder
         */
        B target(Vector value);

        /**
         * Sets the color that the trail particle should have.
         *
         * @param value the trail particle color
         * @return this particle options builder
         * @since 1.0
         */
        B color(RGBLike value);

        /**
         * Sets the time that it should take for the particle to travel from the starting position to the destination.
         *
         * @param value the particle travel time, in ticks
         * @return this particle options builder
         * @since 1.0
         */
        B duration(int value);
    }
}