package net.hypejet.jet.server.registry.codecs.world.particle.scalable.dust;

import net.hypejet.jet.server.registry.codecs.util.color.RGBColorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.scalable.dust.JetDustTransitionParticle;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec}
 * of {@linkplain JetDustTransitionParticle dust transition particles}.
 *
 * @since 1.0
 * @see JetDustTransitionParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class DustTransitionParticleAdditionalCodec
        implements ParticleAdditionalCodec<JetDustTransitionParticle, JetDustTransitionParticle.Builder> {

    private static final String FROM_COLOR_FIELD = "from_color";
    private static final String TO_COLOR_FIELD = "to_color";
    private static final String SCALE_FIELD = "scale";

    /**
     * An instance of the {@linkplain DustTransitionParticleAdditionalCodec dust transition particle additional codec}.
     *
     * @since 1.0
     */
    public static final DustTransitionParticleAdditionalCodec INSTANCE = new DustTransitionParticleAdditionalCodec();

    private DustTransitionParticleAdditionalCodec() {}

    @Override
    public void decode(CompoundBinaryTag compound, JetDustTransitionParticle.Builder particleBuilder) {
        particleBuilder.fromColor(RGBColorBinaryTagCodec.INSTANCE.decode(requiredTag(FROM_COLOR_FIELD, compound)));
        particleBuilder.toColor(RGBColorBinaryTagCodec.INSTANCE.decode(requiredTag(TO_COLOR_FIELD, compound)));
        particleBuilder.scale(requiredTag(SCALE_FIELD, compound, BinaryTagTypes.FLOAT).value());
    }

    @Override
    public void encode(JetDustTransitionParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.put(FROM_COLOR_FIELD, RGBColorBinaryTagCodec.INSTANCE.encode(particle.fromColor()));
        compoundBuilder.put(TO_COLOR_FIELD, RGBColorBinaryTagCodec.INSTANCE.encode(particle.toColor()));
        compoundBuilder.putFloat(SCALE_FIELD, particle.scale());
    }
}