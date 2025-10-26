package net.hypejet.jet.server.registry.codecs.world.particle.trail;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.registry.codecs.util.color.RGBColorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.coordinate.VectorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.trail.TrailParticle;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain TrailParticle trail particles}.
 *
 * @since 1.0
 * @see TrailParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class TrailParticleAdditionalCodec implements ParticleAdditionalCodec<TrailParticle> {

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
    public TrailParticle decode(Holder.Reference<ParticleType> particleType, CompoundBinaryTag compound) {
        return new TrailParticle(
                particleType,
                VectorBinaryTagCodec.INSTANCE.decode(requiredTag(TARGET_FIELD, compound)),
                RGBColorBinaryTagCodec.INSTANCE.decode(requiredTag(COLOR_FIELD, compound)),
                requiredTag(DURATION_FIELD, compound, BinaryTagTypes.INT).value()
        );
    }

    @Override
    public void encode(TrailParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.put(TARGET_FIELD, VectorBinaryTagCodec.INSTANCE.encode(particle.target()));
        compoundBuilder.put(COLOR_FIELD, RGBColorBinaryTagCodec.INSTANCE.encode(particle.color()));
        compoundBuilder.putInt(DURATION_FIELD, particle.travelDuration());
    }
}