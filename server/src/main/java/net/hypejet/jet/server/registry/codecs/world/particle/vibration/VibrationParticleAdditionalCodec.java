package net.hypejet.jet.server.registry.codecs.world.particle.vibration;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.validation.ValidatedBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.coordinate.source.PositionSourceBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.vibration.JetVibrationParticle;
import net.hypejet.jet.world.coordinate.source.EntityPositionSource;
import net.hypejet.jet.world.coordinate.source.PositionSource;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec}
 * of {@linkplain JetVibrationParticle vibration particles}.
 *
 * @since 1.0
 * @see JetVibrationParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class VibrationParticleAdditionalCodec
        implements ParticleAdditionalCodec<JetVibrationParticle, JetVibrationParticle.Builder> {

    private static final String DESTINATION_FIELD = "destination";
    private static final String ARRIVAL_IN_TICKS_FIELD = "arrival_in_ticks";

    private static final BinaryTagCodec<PositionSource> VALIDATED_SOURCE_CODEC = new ValidatedBinaryTagCodec<>(
            PositionSourceBinaryTagCodec.INSTANCE,
            positionSource -> !(positionSource instanceof EntityPositionSource),
            ignored -> new IllegalArgumentException("Entity position sources are not allowed in vibration particles")
    );

    /**
     * An instance of the {@linkplain VibrationParticleAdditionalCodec vibration particle additional codec}.
     *
     * @since 1.0
     */
    public static final VibrationParticleAdditionalCodec INSTANCE = new VibrationParticleAdditionalCodec();

    private VibrationParticleAdditionalCodec() {}

    @Override
    public void decode(CompoundBinaryTag compound, JetVibrationParticle.Builder particleBuilder) {
        particleBuilder.destination(VALIDATED_SOURCE_CODEC.decode(requiredTag(DESTINATION_FIELD, compound)));
        particleBuilder.arrivalDuration(requiredTag(ARRIVAL_IN_TICKS_FIELD, compound, BinaryTagTypes.INT).value());
    }

    @Override
    public void encode(JetVibrationParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.put(DESTINATION_FIELD, VALIDATED_SOURCE_CODEC.encode(particle.destination()));
        compoundBuilder.putInt(ARRIVAL_IN_TICKS_FIELD, particle.arrivalDuration());
    }
}