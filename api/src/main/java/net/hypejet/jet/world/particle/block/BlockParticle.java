package net.hypejet.jet.world.particle.block;

import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.particle.Particle;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain Particle particle} containing a {@linkplain BlockState block state} field.
 *
 * @since 1.0
 * @see BlockState
 * @see Particle
 */
@ApiStatus.NonExtendable
@NullMarked
public interface BlockParticle extends Particle {
    /**
     * Gets the {@linkplain BlockState block state} field value of this particle.
     *
     * @return the block state
     * @since 1.0
     */
    BlockState blockState();

    /**
     * A {@linkplain Particle.Builder particle builder} of a {@linkplain BlockParticle block particle}.
     *
     * @param <P> the type of the particle that the builder is going to create
     * @param <B> the type of this particle builder
     * @since 1.0
     * @see BlockParticle
     * @see Particle.Builder
     */
    interface Builder<P extends BlockParticle, B extends Builder<P, B>> extends Particle.Builder<P> {
        /**
         * Sets the {@linkplain BlockState block state} field value that the particle should have.
         *
         * @param value the block state
         * @return this particle builder
         * @since 1.0
         */
        B blockState(BlockState value);
    }
}