package net.hypejet.jet.server.registry.codecs.world.particle;

import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.world.particle.Particle;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

/**
 * Something handling serialization of additional fields of certain {@linkplain JetParticle particles}.
 *
 * @param <P> the type of particles whose additional-field serialization is handled by this codec
 * @param <B> the type of particle builder creating particles whose serialization is handled by this codec
 * @since 1.0
 * @see JetParticle
 */
@NullMarked
public interface ParticleAdditionalCodec<P extends JetParticle, B extends Particle.Builder<P>> {
    /**
     * Reads the additional particle fields from the specified {@linkplain CompoundBinaryTag compound binary tag}
     * and applies them to the specified {@linkplain Particle.Builder particle builder}.
     *
     * @param compound the compound that contains the additional particle fields
     * @param particleBuilder the particle builder that the decoded particle fields should be applied to
     * @since 1.0
     */
    void decode(CompoundBinaryTag compound, B particleBuilder);

    /**
     * Writes additional particle fields of the specified {@linkplain JetParticle particle}
     * to the specified {@linkplain CompoundBinaryTag.Builder compound binary tag builder}.
     *
     * @param particle the particle whose additional particle fields should be written
     * @param compoundBuilder the compound binary tag builder that the additional fields should be written to
     * @since 1.0
     */
    void encode(P particle, CompoundBinaryTag.Builder compoundBuilder);
}