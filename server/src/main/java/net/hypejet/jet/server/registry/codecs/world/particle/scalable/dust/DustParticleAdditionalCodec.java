package net.hypejet.jet.server.registry.codecs.world.particle.scalable.dust;

import net.hypejet.jet.server.registry.codecs.util.color.RGBColorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.scalable.dust.JetDustParticle;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain JetDustParticle dust particles}.
 *
 * @since 1.0
 * @see JetDustParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class DustParticleAdditionalCodec
        implements ParticleAdditionalCodec<JetDustParticle, JetDustParticle.Builder> {

    private static final String COLOR_FIELD = "color";
    private static final String SCALE_FIELD = "scale";

    /**
     * An instance of the {@linkplain DustParticleAdditionalCodec dust particle additional codec}.
     *
     * @since 1.0
     */
    public static final DustParticleAdditionalCodec INSTANCE = new DustParticleAdditionalCodec();

    private DustParticleAdditionalCodec() {}

    @Override
    public void decode(CompoundBinaryTag compound, JetDustParticle.Builder particleBuilder) {
        particleBuilder.color(RGBColorBinaryTagCodec.INSTANCE.decode(requiredTag(COLOR_FIELD, compound)));
        particleBuilder.scale(requiredTag(SCALE_FIELD, compound, BinaryTagTypes.FLOAT).value());
    }

    @Override
    public void encode(JetDustParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.put(COLOR_FIELD, RGBColorBinaryTagCodec.INSTANCE.encode(particle.color()));
        compoundBuilder.putFloat(SCALE_FIELD, particle.scale());
    }
}