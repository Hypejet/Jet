package net.hypejet.jet.server.registry.codecs.world.particle.shirek;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.shriek.ShriekParticle;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain ShriekParticle shriek particles}.
 *
 * @since 1.0
 * @see ShriekParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class ShriekParticleAdditionalCodec implements ParticleAdditionalCodec<ShriekParticle> {

    private static final String DELAY_FIELD = "delay";

    /**
     * An instance of the {@linkplain ShriekParticleAdditionalCodec shriek particle additional codec}.
     *
     * @since 1.0
     */
    public static final ShriekParticleAdditionalCodec INSTANCE = new ShriekParticleAdditionalCodec();

    private ShriekParticleAdditionalCodec() {}

    @Override
    public ShriekParticle decode(Holder.Reference<ParticleType> particleType, CompoundBinaryTag compound) {
        return new ShriekParticle(particleType, requiredTag(DELAY_FIELD, compound, BinaryTagTypes.INT).value());
    }

    @Override
    public void encode(ShriekParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.putInt(DELAY_FIELD, particle.delay());
    }
}