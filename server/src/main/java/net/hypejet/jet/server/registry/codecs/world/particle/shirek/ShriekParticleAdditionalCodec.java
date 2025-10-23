package net.hypejet.jet.server.registry.codecs.world.particle.shirek;

import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.shriek.JetShriekParticle;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain JetShriekParticle shriek particles}.
 *
 * @since 1.0
 * @see JetShriekParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class ShriekParticleAdditionalCodec
        implements ParticleAdditionalCodec<JetShriekParticle, JetShriekParticle.Builder> {

    private static final String DELAY_FIELD = "delay";

    /**
     * An instance of the {@linkplain ShriekParticleAdditionalCodec shriek particle additional codec}.
     *
     * @since 1.0
     */
    public static final ShriekParticleAdditionalCodec INSTANCE = new ShriekParticleAdditionalCodec();

    private ShriekParticleAdditionalCodec() {}

    @Override
    public void decode(CompoundBinaryTag compound, JetShriekParticle.Builder particleBuilder) {
        particleBuilder.delay(requiredTag(DELAY_FIELD, compound, BinaryTagTypes.INT).value());
    }

    @Override
    public void encode(JetShriekParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.putInt(DELAY_FIELD, particle.delay());
    }
}