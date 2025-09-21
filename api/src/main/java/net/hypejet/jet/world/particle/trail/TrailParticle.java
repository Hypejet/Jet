package net.hypejet.jet.world.particle.trail;

import net.hypejet.jet.util.color.RGBColor;
import net.hypejet.jet.world.coordinate.Vector;
import net.hypejet.jet.world.particle.Particle;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A trial {@linkplain Particle particle}.
 *
 * @since 1.0
 * @see Particle
 */
@ApiStatus.NonExtendable
@NullMarked
public interface TrailParticle extends Particle {
    /**
     * Gets the destination that this trail particle should start travelling to after spawning.
     *
     * @return the destination, as vector
     * @since 1.0
     */
    Vector target();

    /**
     * Gets the color of this trail particle.
     *
     * @return the trail particle color
     * @since 1.0
     */
    RGBColor color();

    /**
     * Gets the time that it takes for this particle to travel
     * from the starting position to the destination.
     *
     * @return the particle travel time, in ticks
     * @since 1.0
     */
    int travelDuration();

    /**
     * A {@linkplain Particle.Builder particle builder} of a {@linkplain TrailParticle trail particle}.
     *
     * @param <P> the type of the particle that the builder is going to create
     * @param <B> the type of this particle builder
     * @since 1.0
     * @see TrailParticle
     * @see Particle.Builder
     */
    interface Builder<P extends TrailParticle, B extends Builder<P, B>> extends Particle.Builder<P> {
        /**
         * Sets the destination that the trail particle should start travelling to after spawning.
         *
         * @param value the destination, as vector
         * @return this particle builder
         */
        B target(Vector value);

        /**
         * Sets the color that the trail particle should have.
         *
         * @param value the trail particle color
         * @return this particle builder
         * @since 1.0
         */
        B color(RGBColor value);

        /**
         * Sets the time that it should take for the particle to travel from the starting position to the destination.
         *
         * @param value the particle travel time, in ticks
         * @return this particle builder
         * @since 1.0
         */
        B travelDuration(int value);
    }
}