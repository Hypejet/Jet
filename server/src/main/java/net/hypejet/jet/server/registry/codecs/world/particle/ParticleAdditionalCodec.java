package net.hypejet.jet.server.registry.codecs.world.particle;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

/**
 * Something handling serialization of additional fields of certain {@linkplain Particle particles}.
 *
 * @param <P> the type of particles whose additional-field serialization is handled by this codec
 * @since 1.0
 * @see Particle
 */
@NullMarked
public interface ParticleAdditionalCodec<P extends Particle> {
    /**
     * Creates a {@linkplain Particle particle} by reading the additional particle
     * fields from the specified {@linkplain CompoundBinaryTag compound binary tag}.
     *
     * @param particleType the particle type of which the particle should be
     * @param compound the compound that contains the additional particle fields
     * @since 1.0
     */
    P decode(Holder.Reference<ParticleType> particleType, CompoundBinaryTag compound);

    /**
     * Writes additional particle fields of the specified {@linkplain Particle particle}
     * to the specified {@linkplain CompoundBinaryTag.Builder compound binary tag builder}.
     *
     * @param particle the particle whose additional particle fields should be written
     * @param compoundBuilder the compound binary tag builder that the additional fields should be written to
     * @since 1.0
     */
    void encode(P particle, CompoundBinaryTag.Builder compoundBuilder);
}