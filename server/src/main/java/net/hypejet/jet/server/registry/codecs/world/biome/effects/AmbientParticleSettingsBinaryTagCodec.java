package net.hypejet.jet.server.registry.codecs.world.biome.effects;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.world.biome.effects.AmbientParticleSettings;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain AmbientParticleSettings ambient particle settings}.
 *
 * @since 1.0
 * @see AmbientParticleSettings
 * @see BinaryTagCodec
 */
@NullMarked
public final class AmbientParticleSettingsBinaryTagCodec implements BinaryTagCodec<AmbientParticleSettings> {

    private static final String OPTIONS_FIELD = "options";
    private static final String PROBABILITY_FIELD = "probability";

    /**
     * An instance
     * of the {@linkplain AmbientParticleSettingsBinaryTagCodec ambient particle settings binary tag codec}.
     *
     * @since 1.0
     */
    public static final AmbientParticleSettingsBinaryTagCodec INSTANCE = new AmbientParticleSettingsBinaryTagCodec();

    @Override
    public AmbientParticleSettings decode(BinaryTag binaryTag) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            return new AmbientParticleSettings(
                    requiredTag(OPTIONS_FIELD, compound),
                    requiredTag(PROBABILITY_FIELD, compound, BinaryTagTypes.FLOAT).value()
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag type must be of compound type to decode it to ambient particle settings"
            );
        }
    }

    @Override
    public BinaryTag encode(AmbientParticleSettings value) {
        return CompoundBinaryTag.builder()
                .put(OPTIONS_FIELD, value.options())
                .putFloat(PROBABILITY_FIELD, value.probability())
                .build();
    }
}