package net.hypejet.jet.server.world.particle;

import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain ParticleType particle type}.
 *
 * @param particleClass the class of particle object supported by this particle type
 * @param networkWriter a network writer of the particle object supported by this particle type
 * @param binaryTagCodec a binary-tag codec of the particle object supported by this particle type
 * @param <P> the type of particle object supported by this particle type
 * @since 1.0
 * @see ParticleType
 */
@NullMarked
public record JetParticleType<P extends Particle>(Class<P> particleClass,
                                                  NetworkWriter<P> networkWriter,
                                                  BinaryTagCodec<P> binaryTagCodec) implements ParticleType {
    /**
     * Constructs the {@linkplain JetParticleType particle type implementation}.
     *
     * @param particleClass the class of particle object that should be supported by the particle type
     * @param networkWriter a network writer of the particle object that should be supported by the particle type
     * @param binaryTagCodec a binary-tag codec of the particle object that should be supported by the particle type
     * @since 1.0
     */
    public JetParticleType {
        Objects.requireNonNull(particleClass, "particle class");
        Objects.requireNonNull(networkWriter, "network writer");
        Objects.requireNonNull(binaryTagCodec, "binary tag codec");
    }
}