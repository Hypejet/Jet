package net.hypejet.jet.server.world.particle.block;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.world.block.BlockState;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.block.BlockParticle;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain BlockParticle block particle}.
 *
 * @since 1.0
 * @see BlockParticle
 */
@NullMarked
public final class JetBlockParticle extends JetParticle implements BlockParticle {

    private final BlockState blockState;

    /**
     * Constructs the {@linkplain JetBlockParticle block particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param blockState the value that the block state field of the particle should have
     * @since 1.0
     */
    private JetBlockParticle(Holder.Reference<ParticleType> particleType, BlockState blockState) {
        super(particleType);
        this.blockState = Objects.requireNonNull(blockState, "block state");
    }

    @Override
    public BlockState blockState() {
        return this.blockState;
    }

    /**
     * An implementation of the {@linkplain BlockParticle.Builder block particle builder}
     * creating {@linkplain JetBlockParticle block particles} without additional options.
     *
     * @since 1.0
     * @see JetBlockParticle
     * @see BlockParticle.Builder
     */
    public static final class Builder implements BlockParticle.Builder<JetBlockParticle, Builder> {

        private final Holder.Reference<ParticleType> particleType;
        private BlockState blockState;

        /**
         * Constructs the {@linkplain Builder block particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @param initialBlockState the block state field value that the block particle should
         *                          have if no other block state was specified in the builder
         * @since 1.0
         */
        public Builder(Holder.Reference<ParticleType> particleType, JetBlockState initialBlockState) {
            this.particleType = Objects.requireNonNull(particleType, "particle type");
            this.blockState = Objects.requireNonNull(initialBlockState, "initial block state");
        }

        @Override
        public Builder blockState(BlockState value) {
            this.blockState = Objects.requireNonNull(value, "value");
            return this;
        }

        @Override
        public JetBlockParticle build() {
            return new JetBlockParticle(this.particleType, this.blockState);
        }
    }
}