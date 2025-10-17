package net.hypejet.jet.world.particle.block;

import net.hypejet.jet.world.block.state.BlockStateReference;
import net.hypejet.jet.world.particle.Particle;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain Particle particle} containing a {@linkplain BlockStateReference block state reference} field.
 *
 * @since 1.0
 * @see BlockStateReference
 * @see Particle
 */
@ApiStatus.NonExtendable
@NullMarked
public interface BlockParticle extends Particle {
    /**
     * Gets the {@linkplain BlockStateReference block state reference} field value of this particle.
     *
     * @return the block state reference
     * @since 1.0
     */
    BlockStateReference blockState();

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
         * Sets the {@linkplain BlockStateReference block state reference} field value that the particle should have.
         *
         * @param value the block state reference
         * @return this particle builder
         * @since 1.0
         */
        B blockState(BlockStateReference value);
    }
}