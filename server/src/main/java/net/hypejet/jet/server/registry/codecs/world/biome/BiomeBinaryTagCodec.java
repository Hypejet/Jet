package net.hypejet.jet.server.registry.codecs.world.biome;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.IndexBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.StringBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.biome.effects.SpecialEffectsBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.biome.climate.ClimateSettings;
import net.hypejet.jet.world.biome.climate.TemperatureModifier;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.booleanValue;
import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Biome biome}.
 *
 * @since 1.0
 * @see Biome
 * @see BinaryTagCodec
 */
public final class BiomeBinaryTagCodec implements BinaryTagCodec<Biome> {

    private static final String HAS_PRECIPITATION_FIELD = "has_precipitation";
    private static final String TEMPERATURE_FIELD = "temperature";
    private static final String TEMPERATURE_MODIFIER_FIELD = "temperature_modifier";
    private static final String DOWNFALL_FIELD = "downfall";
    private static final String SPECIAL_EFFECTS_FIELD = "effects";

    private static final BinaryTagCodec<TemperatureModifier> TEMPERATURE_MODIFIER_CODEC = new IndexBinaryTagCodec<>(
            IndexUtil.fromMap(Map.of(
                    "none", TemperatureModifier.NONE,
                    "frozen", TemperatureModifier.FROZEN
            )),
            StringBinaryTagCodec.INSTANCE
    );

    /**
     * An instance of the {@linkplain BiomeBinaryTagCodec biome binary tag codec}.
     *
     * @since 1.0
     */
    public static final BiomeBinaryTagCodec INSTANCE = new BiomeBinaryTagCodec();

    private BiomeBinaryTagCodec() {}

    @Override
    public @NotNull Biome decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            BinaryTag temperatureModifierTag = compound.get(TEMPERATURE_MODIFIER_FIELD);
            return new Biome(
                    new ClimateSettings(
                            booleanValue(requiredTag(HAS_PRECIPITATION_FIELD, compound, BinaryTagTypes.BYTE)),
                            requiredTag(TEMPERATURE_FIELD, compound, BinaryTagTypes.FLOAT).value(),
                            temperatureModifierTag == null
                                    ? TemperatureModifier.NONE
                                    : TEMPERATURE_MODIFIER_CODEC.decode(temperatureModifierTag),
                            requiredTag(DOWNFALL_FIELD, compound, BinaryTagTypes.FLOAT).value()
                    ),
                    SpecialEffectsBinaryTagCodec.INSTANCE.decode(requiredTag(SPECIAL_EFFECTS_FIELD, compound))
            );
        } else {
            throw new IllegalArgumentException("The encoded tag type must be of compound type to decode it to biome");
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull Biome decoded) throws Exception {
        ClimateSettings climateSettings = decoded.climateSettings();
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putBoolean(HAS_PRECIPITATION_FIELD, climateSettings.hasPrecipitation())
                .putFloat(TEMPERATURE_FIELD, climateSettings.temperature())
                .putFloat(DOWNFALL_FIELD, climateSettings.downfall())
                .put(SPECIAL_EFFECTS_FIELD, SpecialEffectsBinaryTagCodec.INSTANCE.encode(decoded.specialEffects()));


        TemperatureModifier temperatureModifier = climateSettings.temperatureModifier();
        if (temperatureModifier != TemperatureModifier.NONE) {
            builder.put(TEMPERATURE_MODIFIER_FIELD, TEMPERATURE_MODIFIER_CODEC.encode(temperatureModifier));
        }

        return builder.build();
    }
}