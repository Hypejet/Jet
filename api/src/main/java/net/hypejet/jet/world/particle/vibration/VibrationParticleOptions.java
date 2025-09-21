package net.hypejet.jet.world.particle.vibration;

import net.hypejet.jet.world.coordinate.source.PositionSource;
import net.hypejet.jet.world.particle.ParticleOptions;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * {@linkplain ParticleOptions Particle options} of a vibration particle.
 *
 * @since 1.0
 * @see ParticleOptions
 */
@ApiStatus.NonExtendable
@NullMarked
public interface VibrationParticleOptions extends ParticleOptions {
    /**
     * Gets a {@linkplain PositionSource position source} providing the destination
     * that the vibration particle should start travelling to after spawning.
     *
     * @return the position source
     * @since 1.0
     */
    PositionSource destination();

    /**
     * Gets the time that it takes for the vibration particle to travel
     * from the starting position to the specified destination.
     *
     * @return the particle travel time, in ticks
     * @since 1.0
     */
    int arrivalDuration();

    /**
     * A {@linkplain ParticleOptions.Builder particle options builder}
     * of {@linkplain VibrationParticleOptions vibration particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @param <B> the type of this particle options builder
     * @since 1.0
     * @see VibrationParticleOptions
     * @see ParticleOptions.Builder
     */
    interface Builder<O extends VibrationParticleOptions, B extends Builder<O, B>> extends ParticleOptions.Builder<O> {
        /**
         * Sets the {@linkplain PositionSource position source} providing the destination
         * that the vibration particle should start travelling to after spawning.
         *
         * @param value the position source providing the destination
         * @return this particle options builder
         * @since 1.0
         */
        B destination(PositionSource value);

        /**
         * Sets the time that it should take for the vibration particle
         * to travel from the starting position to the specified destination.
         *
         * @param value the particle travel time, in ticks
         * @return this particle options builder
         * @since 1.0
         */
        B arrivalDuration(int value);
    }
}