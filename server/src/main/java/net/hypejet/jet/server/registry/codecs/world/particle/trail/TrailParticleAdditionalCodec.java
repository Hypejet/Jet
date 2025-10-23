package net.hypejet.jet.server.registry.codecs.world.particle.trail;

import net.hypejet.jet.server.registry.codecs.util.color.RGBColorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.coordinate.VectorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.trail.JetTrailParticle;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain JetTrailParticle trail particles}.
 *
 * @since 1.0
 * @see JetTrailParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class TrailParticleAdditionalCodec
        implements ParticleAdditionalCodec<JetTrailParticle, JetTrailParticle.Builder> {

    private static final String TARGET_FIELD = "target";
    private static final String COLOR_FIELD = "color";
    private static final String DURATION_FIELD = "duration";

    /**
     * An instance of the {@linkplain TrailParticleAdditionalCodec trail particle additional codec}.
     *
     * @since 1.0
     */
    public static final TrailParticleAdditionalCodec INSTANCE = new TrailParticleAdditionalCodec();

    private TrailParticleAdditionalCodec() {}

    @Override
    public void decode(CompoundBinaryTag compound, JetTrailParticle.Builder particleBuilder) {
        particleBuilder.target(VectorBinaryTagCodec.INSTANCE.decode(requiredTag(TARGET_FIELD, compound)));
        particleBuilder.color(RGBColorBinaryTagCodec.INSTANCE.decode(requiredTag(COLOR_FIELD, compound)));
        particleBuilder.travelDuration(requiredTag(DURATION_FIELD, compound, BinaryTagTypes.INT).value());
    }

    @Override
    public void encode(JetTrailParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.put(TARGET_FIELD, VectorBinaryTagCodec.INSTANCE.encode(particle.target()));
        compoundBuilder.put(COLOR_FIELD, RGBColorBinaryTagCodec.INSTANCE.encode(particle.color()));
        compoundBuilder.putInt(DURATION_FIELD, particle.travelDuration());
    }
}