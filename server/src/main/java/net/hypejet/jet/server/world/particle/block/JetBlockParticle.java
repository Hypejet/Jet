package net.hypejet.jet.server.world.particle.block;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.keys.BlockKeys;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.world.block.state.BlockStateReference;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.block.BlockParticle;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Objects;

/**
 * An implementation of the {@linkplain BlockParticle block particle}.
 *
 * @since 1.0
 * @see BlockParticle
 */
@NullMarked
public final class JetBlockParticle extends JetParticle implements BlockParticle {

    private final BlockStateReference blockStateReference;

    /**
     * Constructs the {@linkplain JetBlockParticle block particle implementation}.
     *
     * @param particleType the type of which the particle should be
     * @param blockStateReference the value that the block state reference field of the particle should have
     * @since 1.0
     */
    private JetBlockParticle(Holder.Reference<ParticleType> particleType, BlockStateReference blockStateReference) {
        super(particleType);
        this.blockStateReference = Objects.requireNonNull(blockStateReference, "block state reference");
    }

    @Override
    public BlockStateReference blockState() {
        return this.blockStateReference;
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

        private BlockStateReference blockStateReference = new BlockStateReference(
                new Holder.Reference<>(BlockKeys.STONE),
                Map.of()
        );

        /**
         * Constructs the {@linkplain Builder block particle builder implementation}.
         *
         * @param particleType the type of which the particle should be
         * @since 1.0
         */
        public Builder(Holder.Reference<ParticleType> particleType) {
            this.particleType = Objects.requireNonNull(particleType, "particle type");
        }

        @Override
        public Builder blockState(BlockStateReference value) {
            this.blockStateReference = Objects.requireNonNull(value, "value");
            return this;
        }

        @Override
        public JetBlockParticle build() {
            return new JetBlockParticle(this.particleType, this.blockStateReference);
        }
    }
}