package net.hypejet.jet.server.registry.codecs.world.particle.scalable.dust;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.registry.codecs.util.color.RGBColorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.scalable.dust.DustParticle;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain DustParticle dust particles}.
 *
 * @since 1.0
 * @see DustParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class DustParticleAdditionalCodec implements ParticleAdditionalCodec<DustParticle> {

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
    public DustParticle decode(Holder.Reference<ParticleType> particleType, CompoundBinaryTag compound) {
        return new DustParticle(
                particleType,
                requiredTag(SCALE_FIELD, compound, BinaryTagTypes.FLOAT).value(),
                RGBColorBinaryTagCodec.INSTANCE.decode(requiredTag(COLOR_FIELD, compound))
        );
    }

    @Override
    public void encode(DustParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.put(COLOR_FIELD, RGBColorBinaryTagCodec.INSTANCE.encode(particle.color()));
        compoundBuilder.putFloat(SCALE_FIELD, particle.scale());
    }
}