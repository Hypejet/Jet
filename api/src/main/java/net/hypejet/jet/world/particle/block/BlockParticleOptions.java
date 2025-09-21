package net.hypejet.jet.world.particle.block;

import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.particle.ParticleOptions;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
* {@linkplain ParticleOptions Particle options} containing a {@linkplain BlockState block state} field.
 *
 * @since 1.0
 * @see BlockState
 * @see ParticleOptions
 */
@ApiStatus.NonExtendable
@NullMarked
public interface BlockParticleOptions extends ParticleOptions {
    /**
     * Gets the {@linkplain BlockState block state} field value
     * of this {@linkplain BlockParticleOptions block particle options}.
     *
     * @return the block state
     * @since 1.0
     */
    BlockState blockState();

    /**
     * A {@linkplain ParticleOptions.Builder particle options builder}
     * of a {@linkplain BlockParticleOptions block particle options}.
     *
     * @param <O> the type of the particle options that the builder is going to create
     * @param <B> the type of this particle options builder
     * @since 1.0
     * @see BlockParticleOptions
     * @see ParticleOptions.Builder
     */
    interface Builder<O extends BlockParticleOptions, B extends Builder<O, B>> extends ParticleOptions.Builder<O> {
        /**
         * Sets the {@linkplain BlockState block state} field value that
         * the {@linkplain BlockParticleOptions block particle options} should have.
         *
         * @param value the block state
         * @return this particle options builder
         * @since 1.0
         */
        B blockState(BlockState value);
    }
}