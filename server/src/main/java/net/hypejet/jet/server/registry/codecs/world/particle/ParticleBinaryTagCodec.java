package net.hypejet.jet.server.registry.codecs.world.particle;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.keys.ParticleTypeKeys;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.block.BlockParticleAdditionalCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.color.ColorParticleAdditionalCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.item.ItemParticleAdditionalCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.scalable.dust.DustParticleAdditionalCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.scalable.dust.DustTransitionParticleAdditionalCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.sculk.SculkChargeParticleAdditionalCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.shirek.ShriekParticleAdditionalCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.trail.TrailParticleAdditionalCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.vibration.VibrationParticleAdditionalCodec;
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.block.BlockParticle;
import net.hypejet.jet.world.particle.color.ColorParticle;
import net.hypejet.jet.world.particle.item.ItemParticle;
import net.hypejet.jet.world.particle.scalable.dust.DustParticle;
import net.hypejet.jet.world.particle.scalable.dust.DustTransitionParticle;
import net.hypejet.jet.world.particle.sculk.SculkChargeParticle;
import net.hypejet.jet.world.particle.shriek.ShriekParticle;
import net.hypejet.jet.world.particle.simple.SimpleParticle;
import net.hypejet.jet.world.particle.trail.TrailParticle;
import net.hypejet.jet.world.particle.vibration.VibrationParticle;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Objects;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain Particle particles}.
 *
 * @since 1.0
 * @see Particle
 * @see BinaryTagCodec
 */
@NullMarked
public final class ParticleBinaryTagCodec implements BinaryTagCodec<Particle> {

    private static final String TYPE_FIELD = "type";

    private static final ParticleCodecType<?> BLOCK_PARTICLE_CODEC_TYPE = new ParticleCodecType<>(
            BlockParticleAdditionalCodec.INSTANCE,
            BlockParticle.class
    );

    private static final ParticleCodecType<?> COLOR_PARTICLE_CODEC_TYPE = new ParticleCodecType<>(
            ColorParticleAdditionalCodec.INSTANCE,
            ColorParticle.class
    );

    private static final Map<Key, ParticleCodecType<?>> PARTICLE_CODEC_TYPES = Map.ofEntries(
            Map.entry(ParticleTypeKeys.BLOCK, BLOCK_PARTICLE_CODEC_TYPE),
            Map.entry(ParticleTypeKeys.BLOCK_MARKER, BLOCK_PARTICLE_CODEC_TYPE),
            Map.entry(ParticleTypeKeys.FALLING_DUST, BLOCK_PARTICLE_CODEC_TYPE),
            Map.entry(ParticleTypeKeys.ENTITY_EFFECT, COLOR_PARTICLE_CODEC_TYPE),
            Map.entry(ParticleTypeKeys.TINTED_LEAVES, COLOR_PARTICLE_CODEC_TYPE),
            Map.entry(ParticleTypeKeys.DUST_PILLAR, COLOR_PARTICLE_CODEC_TYPE),
            Map.entry(ParticleTypeKeys.BLOCK_CRUMBLE, COLOR_PARTICLE_CODEC_TYPE),
            Map.entry(
                    ParticleTypeKeys.DUST,
                    new ParticleCodecType<>(
                            DustParticleAdditionalCodec.INSTANCE, DustParticle.class
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.DUST_COLOR_TRANSITION,
                    new ParticleCodecType<>(
                            DustTransitionParticleAdditionalCodec.INSTANCE, DustTransitionParticle.class
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.SCULK_CHARGE,
                    new ParticleCodecType<>(
                            SculkChargeParticleAdditionalCodec.INSTANCE, SculkChargeParticle.class
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.ITEM,
                    new ParticleCodecType<>(
                            ItemParticleAdditionalCodec.INSTANCE, ItemParticle.class
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.VIBRATION,
                    new ParticleCodecType<>(
                            VibrationParticleAdditionalCodec.INSTANCE, VibrationParticle.class
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.TRAIL,
                    new ParticleCodecType<>(
                            TrailParticleAdditionalCodec.INSTANCE, TrailParticle.class
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.SHRIEK,
                    new ParticleCodecType<>(
                            ShriekParticleAdditionalCodec.INSTANCE, ShriekParticle.class
                    )
            )
    );

    /**
     * An instance of the {@linkplain ParticleBinaryTagCodec particle binary-tag codec}.
     *
     * @since 1.0
     */
    public static final ParticleBinaryTagCodec INSTANCE = new ParticleBinaryTagCodec();

    private ParticleBinaryTagCodec() {}

    @Override
    public Particle decode(BinaryTag binaryTag) {
        if (!(binaryTag instanceof CompoundBinaryTag compound)) {
            throw new IllegalArgumentException(
                    "The encoded binary tag must be of compound type to decode it into a particle"
            );
        }

        Key typeKey = KeyBinaryTagCodec.INSTANCE.decode(requiredTag(TYPE_FIELD, compound));
        Holder.Reference<ParticleType> particleType = new Holder.Reference<>(typeKey);
        ParticleCodecType<?> codecType = PARTICLE_CODEC_TYPES.get(typeKey);

        if (codecType == null) {
            return new SimpleParticle(particleType);
        } else {
            return codecType.codec().decode(particleType, compound);
        }
    }

    @Override
    public BinaryTag encode(Particle value) {
        CompoundBinaryTag.Builder tagBuilder = CompoundBinaryTag.builder();
        Key particleTypeKey = value.particleType().key();

        tagBuilder.put(TYPE_FIELD, KeyBinaryTagCodec.INSTANCE.encode(particleTypeKey));
        ParticleCodecType<?> codecType = PARTICLE_CODEC_TYPES.get(particleTypeKey);
        if (codecType != null) encodeAdditional(value, codecType, tagBuilder);

        return tagBuilder.build();
    }

    private static <P extends Particle> void encodeAdditional(Particle particle,
                                                              ParticleCodecType<P> codecType,
                                                              CompoundBinaryTag.Builder tagBuilder) {
        Class<P> particleClass = codecType.particleClass();
        if (!particleClass.isAssignableFrom(particle.getClass())) {
            throw new IllegalArgumentException(String.format(
                    "The particle with %s type must be of %s class to encode it",
                    particle.particleType().key(),
                    particleClass.getName()
            ));
        }
        codecType.codec().encode(particleClass.cast(particle), tagBuilder);
    }

    /**
     * A type of how serialization of {@linkplain Particle particles} with additional data should be handled.
     *
     * @param codec the particle additional codec that the additional
     *              particle fields should be read and written with
     * @param particleClass the class of particles whose serialization is
     *                      handled by the specified particle additional codec
     * @param <P> the type of particles whose serialization is handled by the particle additional codec
     * @since 1.0
     * @see ParticleAdditionalCodec
     * @see Particle
     */
    private record ParticleCodecType<P extends Particle>(ParticleAdditionalCodec<P> codec, Class<P> particleClass) {
        /**
         * Constructs the {@linkplain ParticleCodecType particle codec type}.
         *
         * @param codec the particle additional codec that the additional
         *              particle fields should be read and written with
         * @param particleClass the class of particles whose serialization is
         *                      handled by the specified particle additional codec
         * @since 1.0
         */
        public ParticleCodecType {
            Objects.requireNonNull(codec, "codec");
            Objects.requireNonNull(particleClass, "particle class");
        }
    }
}