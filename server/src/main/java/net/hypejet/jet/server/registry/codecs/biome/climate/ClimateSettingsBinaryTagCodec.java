package net.hypejet.jet.server.registry.codecs.biome.climate;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.IndexBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.StringBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
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
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain ClimateSettings climate settings}.
 *
 * @since 1.0
 * @see ClimateSettings
 * @see BinaryTagCodec
 */
public final class ClimateSettingsBinaryTagCodec implements BinaryTagCodec<ClimateSettings> {

    private static final String HAS_PRECIPITATION_FIELD = "has_precipitation";
    private static final String TEMPERATURE_FIELD = "temperature";
    private static final String TEMPERATURE_MODIFIER_FIELD = "temperature_modifier";
    private static final String DOWNFALL_FIELD = "downfall";

    private static final BinaryTagCodec<TemperatureModifier> TEMPERATURE_MODIFIER_CODEC = new IndexBinaryTagCodec<>(
            IndexUtil.fromMap(Map.of(
                    "none", TemperatureModifier.NONE,
                    "frozen", TemperatureModifier.FROZEN
            )),
            StringBinaryTagCodec.INSTANCE
    );

    /**
     * An instance of the {@linkplain ClimateSettingsBinaryTagCodec climate settings binary tag codec}.
     *
     * @since 1.0
     */
    public static final ClimateSettingsBinaryTagCodec INSTANCE = new ClimateSettingsBinaryTagCodec();

    private ClimateSettingsBinaryTagCodec() {}

    @Override
    public @NotNull ClimateSettings decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag tag) {
            return new ClimateSettings(
                    booleanValue(requiredTag(HAS_PRECIPITATION_FIELD, tag, BinaryTagTypes.BYTE)),
                    requiredTag(TEMPERATURE_FIELD, tag, BinaryTagTypes.FLOAT).value(),
                    TEMPERATURE_MODIFIER_CODEC.decode(requiredTag(TEMPERATURE_MODIFIER_FIELD, tag)),
                    requiredTag(DOWNFALL_FIELD, tag, BinaryTagTypes.FLOAT).value()
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag type must be of compound type to decode it to climate settings"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull ClimateSettings decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .putBoolean(HAS_PRECIPITATION_FIELD, decoded.hasPrecipitation())
                .putFloat(TEMPERATURE_FIELD, decoded.temperature())
                .put(TEMPERATURE_MODIFIER_FIELD, TEMPERATURE_MODIFIER_CODEC.encode(decoded.temperatureModifier()))
                .putFloat(DOWNFALL_FIELD, decoded.downfall())
                .build();
    }
}