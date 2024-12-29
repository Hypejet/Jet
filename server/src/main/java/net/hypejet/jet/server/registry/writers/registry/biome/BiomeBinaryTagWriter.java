package net.hypejet.jet.server.registry.writers.registry.biome;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.biome.temperature.BiomeTemperatureModifier;
import net.hypejet.jet.server.registry.writers.mapper.MapperBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.registry.biome.effects.BiomeEffectSettingsBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain Biome a biome} into
 * {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see Biome
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class BiomeBinaryTagWriter implements Writer<Biome, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain BiomeBinaryTagWriter biome binary tag writer}.
     *
     * @since 1.0
     */
    public static final BiomeBinaryTagWriter INSTANCE = new BiomeBinaryTagWriter();

    private static final String HAS_PRECIPITATION = "has_precipitation";
    private static final String TEMPERATURE = "temperature";
    private static final String TEMPERATURE_MODIFIER = "temperature_modifier";
    private static final String DOWNFALL = "downfall";
    private static final String EFFECTS = "effects";

    private static final Writer<BiomeTemperatureModifier, StringBinaryTag> TEMPERATURE_MODIFIER_CODEC =
            MapperBinaryTagWriter.stringCodec(Mapper.builder(BiomeTemperatureModifier.class, String.class)
                    .register(BiomeTemperatureModifier.NONE, "none")
                    .register(BiomeTemperatureModifier.FROZEN, "frozen")
                    .build());

    private BiomeBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull Biome object) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putBoolean(HAS_PRECIPITATION, object.hasPrecipitation())
                .putFloat(TEMPERATURE, object.temperature());

        BiomeTemperatureModifier temperatureModifier = object.temperatureModifier();
        if (temperatureModifier != null)
            builder.put(TEMPERATURE_MODIFIER, TEMPERATURE_MODIFIER_CODEC.write(temperatureModifier));

        return builder.putFloat(DOWNFALL, object.downfall())
                .put(EFFECTS, BiomeEffectSettingsBinaryTagWriter.INSTANCE.write(object.effects()))
                .build();
    }
}