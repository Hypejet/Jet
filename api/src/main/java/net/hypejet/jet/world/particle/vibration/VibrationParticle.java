package net.hypejet.jet.world.particle.vibration;

import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.particle.Particle;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A vibration {@linkplain Particle particle}.
 *
 * @since 1.0
 * @see Particle
 */
@ApiStatus.NonExtendable
@NullMarked
public interface VibrationParticle extends Particle {
    /**
     * Gets a {@linkplain BlockPosition block position} of the destination
     * that this vibration particle should start travelling to after spawning.
     *
     * @return the destination block position
     * @since 1.0
     */
    BlockPosition destination();

    /**
     * Gets the time that it takes for this vibration particle to travel
     * from the starting position to the specified destination.
     *
     * @return the particle travel time, in ticks
     * @since 1.0
     */
    int arrivalDuration();

    /**
     * A {@linkplain Particle.Builder particle builder} of a {@linkplain VibrationParticle vibration particle}.
     *
     * @param <P> the type of the particle that the builder is going to create
     * @param <B> the type of this particle builder
     * @since 1.0
     * @see VibrationParticle
     * @see Particle.Builder
     */
    interface Builder<P extends VibrationParticle, B extends Builder<P, B>> extends Particle.Builder<P> {
        /**
         * Sets the {@linkplain BlockPosition block position} of the destination
         * that the vibration particle should start travelling to after spawning.
         *
         * @param value the destination block position
         * @return this particle builder
         * @since 1.0
         */
        B destination(BlockPosition value);

        /**
         * Sets the time that it should take for the vibration particle
         * to travel from the starting position to the specified destination.
         *
         * @param value the particle travel time, in ticks
         * @return this particle builder
         * @since 1.0
         */
        B arrivalDuration(int value);
    }
}