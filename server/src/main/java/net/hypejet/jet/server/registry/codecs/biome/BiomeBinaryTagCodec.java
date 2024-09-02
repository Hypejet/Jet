package net.hypejet.jet.server.registry.codecs.biome;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.data.model.registry.registries.biome.Biome;
import net.hypejet.jet.data.model.registry.registries.biome.temperature.BiomeTemperatureModifier;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.biome.effects.BiomeEffectSettingsBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.mapper.MapperBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec}, which reads and writes a {@linkplain Biome biome}.
 *
 * @since 1.0
 * @author Codestech
 * @see Biome
 * @see BinaryTagCodec
 */
public final class BiomeBinaryTagCodec implements BinaryTagCodec<Biome> {

    private static final BiomeBinaryTagCodec INSTANCE = new BiomeBinaryTagCodec();

    private static final String HAS_PRECIPITATION = "has_precipitation";
    private static final String TEMPERATURE = "temperature";
    private static final String TEMPERATURE_MODIFIER = "temperature_modifier";
    private static final String DOWNFALL = "downfall";
    private static final String EFFECTS = "effects";

    private static final BinaryTagCodec<BiomeTemperatureModifier> TEMPERATURE_MODIFIER_CODEC = MapperBinaryTagCodec
            .stringCodec(Mapper.builder(BiomeTemperatureModifier.class, String.class)
                    .register(BiomeTemperatureModifier.NONE, "none")
                    .register(BiomeTemperatureModifier.FROZEN, "frozen")
                    .build());

    private BiomeBinaryTagCodec() {}

    @Override
    public @NonNull Biome read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag is not a compound");

        BiomeTemperatureModifier temperatureModifier = null;

        BinaryTag binaryTemperatureModifier = compound.get(TEMPERATURE_MODIFIER);
        if (binaryTemperatureModifier != null)
            temperatureModifier = TEMPERATURE_MODIFIER_CODEC.read(binaryTemperatureModifier);

        BinaryTag binaryEffects = compound.get(EFFECTS);
        if (binaryEffects == null)
            throw new IllegalArgumentException("The biome effect settings were not specified");

        return Biome.builder()
                .downfall(compound.getFloat(DOWNFALL))
                .hasPrecipitation(compound.getBoolean(HAS_PRECIPITATION))
                .temperature(compound.getFloat(TEMPERATURE))
                .temperatureModifier(temperatureModifier)
                .effectSettings(BiomeEffectSettingsBinaryTagCodec.instance().read(binaryEffects))
                .build();
    }

    @Override
    public @NonNull BinaryTag write(@NonNull Biome object) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putBoolean(HAS_PRECIPITATION, object.hasPrecipitation())
                .putFloat(TEMPERATURE, object.temperature());

        BiomeTemperatureModifier temperatureModifier = object.temperatureModifier();
        if (temperatureModifier != null)
            builder.put(TEMPERATURE_MODIFIER, TEMPERATURE_MODIFIER_CODEC.write(temperatureModifier));

        return builder.putFloat(DOWNFALL, object.downfall())
                .put(EFFECTS, BiomeEffectSettingsBinaryTagCodec.instance().write(object.effects()))
                .build();
    }

    /**
     * Gets an instance of the {@linkplain BiomeBinaryTagCodec biome binary-tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BiomeBinaryTagCodec instance() {
        return INSTANCE;
    }
}