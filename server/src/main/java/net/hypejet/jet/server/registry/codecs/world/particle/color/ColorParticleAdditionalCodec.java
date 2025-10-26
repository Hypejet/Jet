package net.hypejet.jet.server.registry.codecs.world.particle.color;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.registry.codecs.util.color.ARGBColorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.color.ColorParticle;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain ColorParticle color particles}.
 *
 * @since 1.0
 * @see ColorParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class ColorParticleAdditionalCodec implements ParticleAdditionalCodec<ColorParticle> {

    private static final String COLOR_FIELD = "color";

    /**
     * An instance of the {@linkplain ColorParticleAdditionalCodec color particle additional codec}.
     *
     * @since 1.0
     */
    public static final ColorParticleAdditionalCodec INSTANCE = new ColorParticleAdditionalCodec();

    private ColorParticleAdditionalCodec() {}

    @Override
    public ColorParticle decode(Holder.Reference<ParticleType> particleType, CompoundBinaryTag compound) {
        return new ColorParticle(
                particleType,
                ARGBColorBinaryTagCodec.INSTANCE.decode(requiredTag(COLOR_FIELD, compound))
        );
    }

    @Override
    public void encode(ColorParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.put(COLOR_FIELD, ARGBColorBinaryTagCodec.INSTANCE.encode(particle.color()));
    }
}