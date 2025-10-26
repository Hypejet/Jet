package net.hypejet.jet.world.particle.block;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.block.state.BlockStateReference;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A {@linkplain Particle particle} containing a {@linkplain BlockStateReference block state reference} field.
 *
 * @param particleType a holder referencing to a particle type of this particle
 * @param blockStateReference the block state reference field value of this particle
 * @since 1.0
 * @see BlockStateReference
 * @see Particle
 */
@NullMarked
public record BlockParticle(Holder.Reference<ParticleType> particleType,
                            BlockStateReference blockStateReference) implements Particle {
    /**
     * Constructs the {@linkplain BlockParticle block particle}.
     *
     * @param particleType a holder referencing to the particle type of which the particle should be
     * @param blockStateReference the value that the block state reference field of the particle should have
     * @since 1.0
     */
    public BlockParticle {
        Objects.requireNonNull(particleType, "particle type");
        Objects.requireNonNull(blockStateReference, "block state reference");
    }
}