package net.hypejet.jet.server.registry.codecs.world.particle.vibration;

import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.coordinate.BlockPositionBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.vibration.JetVibrationParticle;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.kyori.adventure.key.Key;
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

    private static final String POSITION_SOURCE_TYPE_FIELD = "type";
    private static final String POSITION_SOURCE_POS_FIELD = "pos";

    private static final Key BLOCK_POSITION_SOURCE_TYPE_KEY = Key.key("block");

    /**
     * An instance of the {@linkplain VibrationParticleAdditionalCodec vibration particle additional codec}.
     *
     * @since 1.0
     */
    public static final VibrationParticleAdditionalCodec INSTANCE = new VibrationParticleAdditionalCodec();

    private VibrationParticleAdditionalCodec() {}

    @Override
    public void decode(CompoundBinaryTag compound, JetVibrationParticle.Builder particleBuilder) {
        particleBuilder.destination(decodeDestination(
                requiredTag(DESTINATION_FIELD, compound, BinaryTagTypes.COMPOUND)
        ));
        particleBuilder.arrivalDuration(requiredTag(ARRIVAL_IN_TICKS_FIELD, compound, BinaryTagTypes.INT).value());
    }

    @Override
    public void encode(JetVibrationParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.put(DESTINATION_FIELD, encodeDestination(particle.destination()));
        compoundBuilder.putInt(ARRIVAL_IN_TICKS_FIELD, particle.arrivalDuration());
    }

    private static BlockPosition decodeDestination(CompoundBinaryTag compound) {
        Key typeKey = KeyBinaryTagCodec.INSTANCE.decode(requiredTag(POSITION_SOURCE_TYPE_FIELD, compound));
        if (!BLOCK_POSITION_SOURCE_TYPE_KEY.equals(typeKey))
            throw new IllegalArgumentException("Only block position sources are allowed in vibration particles");
        return BlockPositionBinaryTagCodec.INSTANCE.decode(requiredTag(POSITION_SOURCE_POS_FIELD, compound));
    }

    private static CompoundBinaryTag encodeDestination(BlockPosition position) {
        return CompoundBinaryTag.builder()
                .put(POSITION_SOURCE_TYPE_FIELD, KeyBinaryTagCodec.INSTANCE.encode(BLOCK_POSITION_SOURCE_TYPE_KEY))
                .put(POSITION_SOURCE_POS_FIELD, BlockPositionBinaryTagCodec.INSTANCE.encode(position))
                .build();
    }
}