package net.hypejet.jet.server.registry.codecs.world.particle.sculk;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.sculk.SculkChargeParticle;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec}
 * of {@linkplain SculkChargeParticle sculk charge particles}.
 *
 * @since 1.0
 * @see SculkChargeParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class SculkChargeParticleAdditionalCodec implements ParticleAdditionalCodec<SculkChargeParticle> {

    private static final String ROLL_FIELD = "roll";

    /**
     * An instance of the {@linkplain SculkChargeParticleAdditionalCodec sculk charge particle additional codec}.
     *
     * @since 1.0
     */
    public static final SculkChargeParticleAdditionalCodec INSTANCE = new SculkChargeParticleAdditionalCodec();

    @Override
    public SculkChargeParticle decode(Holder.Reference<ParticleType> particleType, CompoundBinaryTag compound) {
        return new SculkChargeParticle(particleType, requiredTag(ROLL_FIELD, compound, BinaryTagTypes.FLOAT).value());
    }

    @Override
    public void encode(SculkChargeParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.putFloat(ROLL_FIELD, particle.roll());
    }
}