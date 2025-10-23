package net.hypejet.jet.server.registry.codecs.world.particle.color;

import net.hypejet.jet.server.registry.codecs.util.color.ARGBColorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.color.JetColorParticle;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain JetColorParticle color particles}.
 *
 * @since 1.0
 * @see JetColorParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class ColorParticleAdditionalCodec
        implements ParticleAdditionalCodec<JetColorParticle, JetColorParticle.Builder> {

    private static final String COLOR_FIELD = "color";

    /**
     * An instance of the {@linkplain ColorParticleAdditionalCodec color particle additional codec}.
     *
     * @since 1.0
     */
    public static final ColorParticleAdditionalCodec INSTANCE = new ColorParticleAdditionalCodec();

    private ColorParticleAdditionalCodec() {}

    @Override
    public void decode(CompoundBinaryTag compound, JetColorParticle.Builder particleBuilder) {
        particleBuilder.color(ARGBColorBinaryTagCodec.INSTANCE.decode(requiredTag(COLOR_FIELD, compound)));
    }

    @Override
    public void encode(JetColorParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.put(COLOR_FIELD, ARGBColorBinaryTagCodec.INSTANCE.encode(particle.color()));
    }
}