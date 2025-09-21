package net.hypejet.jet.world.particle.color;

import net.hypejet.jet.world.particle.ParticleOptions;
import net.kyori.adventure.util.ARGBLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * {@linkplain ParticleOptions Particle options} containing an {@linkplain ARGBLike ARGB-like color}.
 *
 * @since 1.0
 * @see ARGBLike
 * @see ParticleOptions
 */
// TODO: Replace RGBLike with custom color record implementing ARGBLike?
@ApiStatus.NonExtendable
@NullMarked
public interface ColorParticleOptions extends ParticleOptions {
    /**
     * Gets the {@linkplain ARGBLike ARGB-like color} of the particle.
     *
     * @return the ARGB-like color of the particle
     * @since 1.0
     */
    ARGBLike color();

    /**
     * A {@linkplain ParticleOptions.Builder particle options builder}
     * of a {@linkplain ColorParticleOptions color particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @param <B> the type of this particle options builder
     * @since 1.0
     * @see ColorParticleOptions
     * @see ParticleOptions.Builder
     */
    interface Builder<O extends ColorParticleOptions, B extends Builder<O, B>> extends ParticleOptions.Builder<O> {
        /**
         * Sets {@linkplain ARGBLike ARGB-like color} that the particle should have.
         *
         * @param value the ARGB-like color
         * @return this particle options builder
         * @since 1.0
         */
        B color(ARGBLike value);
    }
}