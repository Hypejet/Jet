package net.hypejet.jet.server.registry.codecs.world.particle.sculk;

import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.sculk.JetSculkChargeParticle;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec}
 * of {@linkplain JetSculkChargeParticle sculk charge particles}.
 *
 * @since 1.0
 * @see JetSculkChargeParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class SculkChargeParticleAdditionalCodec
        implements ParticleAdditionalCodec<JetSculkChargeParticle, JetSculkChargeParticle.Builder> {

    private static final String ROLL_FIELD = "roll";

    /**
     * An instance of the {@linkplain SculkChargeParticleAdditionalCodec sculk charge particle additional codec}.
     *
     * @since 1.0
     */
    public static final SculkChargeParticleAdditionalCodec INSTANCE = new SculkChargeParticleAdditionalCodec();

    @Override
    public void decode(CompoundBinaryTag compound, JetSculkChargeParticle.Builder particleBuilder) {
        particleBuilder.roll(requiredTag(ROLL_FIELD, compound, BinaryTagTypes.FLOAT).value());
    }

    @Override
    public void encode(JetSculkChargeParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.putFloat(ROLL_FIELD, particle.roll());
    }
}