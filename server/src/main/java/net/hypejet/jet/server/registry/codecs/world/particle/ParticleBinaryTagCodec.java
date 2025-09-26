package net.hypejet.jet.server.registry.codecs.world.particle;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.keys.ParticleTypeKeys;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.scalable.dust.DustParticleAdditionalCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.scalable.dust.DustTransitionParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.server.world.particle.scalable.dust.JetDustParticle;
import net.hypejet.jet.server.world.particle.scalable.dust.JetDustTransitionParticle;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain JetParticle particles}.
 *
 * @since 1.0
 * @see JetParticle
 * @see BinaryTagCodec
 */
@NullMarked
public final class ParticleBinaryTagCodec implements BinaryTagCodec<JetParticle> {

    private static final String TYPE_FIELD = "type";

    private static final Map<Key, ParticleCodecType<?, ?>> PARTICLE_CODEC_TYPES = Map.ofEntries(
            Map.entry(
                    ParticleTypeKeys.DUST,
                    new ParticleCodecType<>(
                            DustParticleAdditionalCodec.INSTANCE,
                            JetDustParticle.Builder::new,
                            JetDustParticle.class
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.DUST_COLOR_TRANSITION,
                    new ParticleCodecType<>(
                            DustTransitionParticleAdditionalCodec.INSTANCE,
                            JetDustTransitionParticle.Builder::new,
                            JetDustTransitionParticle.class
                    )
            )
            // TODO: Implement other types
    );

    /**
     * An instance of the {@linkplain ParticleBinaryTagCodec particle binary-tag codec}.
     *
     * @since 1.0
     */
    public static final ParticleBinaryTagCodec INSTANCE = new ParticleBinaryTagCodec();

    private ParticleBinaryTagCodec() {}

    @Override
    public JetParticle decode(BinaryTag binaryTag) {
        if (!(binaryTag instanceof CompoundBinaryTag compound)) {
            throw new IllegalArgumentException(
                    "The encoded binary tag must be of compound type to decode it into a particle"
            );
        }

        Key typeKey = KeyBinaryTagCodec.INSTANCE.decode(requiredTag(TYPE_FIELD, compound));
        ParticleCodecType<?, ?> codecType = PARTICLE_CODEC_TYPES.get(typeKey);

        if (codecType == null) {
            return new JetParticle.Builder(new Holder.Reference<>(typeKey)).build();
        } else {
            return decodeAdditional(typeKey, codecType, compound);
        }
    }

    @Override
    public BinaryTag encode(JetParticle value) {
        CompoundBinaryTag.Builder tagBuilder = CompoundBinaryTag.builder();
        Key particleTypeKey = value.particleType().key();

        tagBuilder.put(TYPE_FIELD, KeyBinaryTagCodec.INSTANCE.encode(particleTypeKey));
        ParticleCodecType<?, ?> codecType = PARTICLE_CODEC_TYPES.get(particleTypeKey);
        if (codecType != null) encodeAdditional(value, codecType, tagBuilder);

        return tagBuilder.build();
    }

    private static <P extends JetParticle, B extends Particle.Builder<P>> P decodeAdditional(
            Key typeKey,
            ParticleCodecType<P, B> codecType,
            CompoundBinaryTag compound
    ) {
        B builder = codecType.builderFactory().apply(new Holder.Reference<>(typeKey));
        codecType.codec().decode(compound, builder);
        return builder.build();
    }

    private static <P extends JetParticle> void encodeAdditional(JetParticle particle,
                                                                 ParticleCodecType<P, ?> codecType,
                                                                CompoundBinaryTag.Builder tagBuilder) {
        Class<P> particleClass = codecType.particleClass();
        if (!particleClass.isAssignableFrom(particle.getClass())) {
            throw new IllegalArgumentException(String.format(
                    "The particle with %s type must be of %s class to decode it",
                    particle.particleType().key(),
                    particleClass.getName()
            ));
        }
        codecType.codec().encode(particleClass.cast(particle), tagBuilder);
    }

    /**
     * A type of how {@linkplain JetParticle particles} with additional data should be encoded.
     *
     * @param codec the particle additional codec that the additional particle fields should be written with
     * @param builderFactory a function creating builders of particles (whose serialization
     *                       is handled by the specified particle additional codec) for deserialization purposes
     * @param particleClass the class of particles whose serialization is
     *                      handled by the specified particle additional codec
     * @param <P> the type of particles whose serialization is handled by the particle additional codec
     * @param <B> the type of builder of particles whose serialization is handled by the particle additional codec
     * @since 1.0
     * @see ParticleAdditionalCodec
     * @see JetParticle
     */
    private record ParticleCodecType<P extends JetParticle, B extends Particle.Builder<P>>(
            ParticleAdditionalCodec<P, B> codec,
            Function<Holder.Reference<ParticleType>, B> builderFactory,
            Class<P> particleClass
    ) {
        /**
         * Constructs the {@linkplain ParticleCodecType particle codec type}.
         *
         * @param codec the particle additional codec that the additional particle fields should be written with
         * @param builderFactory a function that should create builders of particles (whose serialization
         *                       is handled by the specified particle additional codec) for deserialization purposes
         * @param particleClass the class of particles whose serialization is
         *                      handled by the specified particle additional codec
         * @since 1.0
         */
        public ParticleCodecType {
            Objects.requireNonNull(codec, "codec");
            Objects.requireNonNull(builderFactory, "builder factory");
            Objects.requireNonNull(particleClass, "particle class");
        }
    }
}