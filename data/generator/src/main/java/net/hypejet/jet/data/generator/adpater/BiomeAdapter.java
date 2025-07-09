package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.model.JsonWeighted;
import net.hypejet.jet.data.json.model.biome.JsonAdditionsSound;
import net.hypejet.jet.data.json.model.biome.JsonAmbientMoodSound;
import net.hypejet.jet.data.json.model.biome.JsonBiome;
import net.hypejet.jet.data.json.model.biome.JsonBiomeSpecialEffects;
import net.hypejet.jet.data.json.model.biome.JsonClimateSettings;
import net.hypejet.jet.data.json.model.biome.JsonGrassColorModifier;
import net.hypejet.jet.data.json.model.biome.JsonMusic;
import net.hypejet.jet.data.json.model.biome.JsonTemperatureModifier;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents something converting {@linkplain Biome biomes} to a Jet data equivalent.
 *
 * @since 1.0
 * @see Biome
 * @see JsonBiome
 */
public final class BiomeAdapter {

    private static final Field CLIMATE_SETTINGS_FIELD;
    private static final Field TEMPERATURE_MODIFIER_FIELD;
    private static final Field DOWNFALL_FIELD;

    static {
        try {
            CLIMATE_SETTINGS_FIELD = Biome.class.getDeclaredField("climateSettings");
            Class<?> climateSettingsClass = Class.forName(Biome.class.getName() + "$ClimateSettings");
            TEMPERATURE_MODIFIER_FIELD = climateSettingsClass.getDeclaredField("temperatureModifier");
            DOWNFALL_FIELD = climateSettingsClass.getDeclaredField("downfall");

            CLIMATE_SETTINGS_FIELD.setAccessible(true);
            TEMPERATURE_MODIFIER_FIELD.setAccessible(true);
            DOWNFALL_FIELD.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    private BiomeAdapter() {}

    /**
     * Converts the specified {@linkplain Biome biome} to a Jet data equivalent.
     *
     * @param biome the biome to convert
     * @return the converted biome
     * @since 1.0
     */
    public static @NonNull JsonBiome convert(@NonNull Biome biome) {
        return new JsonBiome(convertClimateSettings(biome), convertSpecialEffects(biome.getSpecialEffects()));
    }

    private static @NonNull JsonClimateSettings convertClimateSettings(@NonNull Biome biome) {


        Biome.TemperatureModifier temperatureModifier;
        float downfall;

        try {
            Object climateSettings = CLIMATE_SETTINGS_FIELD.get(biome);
            temperatureModifier = (Biome.TemperatureModifier) TEMPERATURE_MODIFIER_FIELD.get(climateSettings);
            downfall = DOWNFALL_FIELD.getFloat(climateSettings);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }

        return new JsonClimateSettings(
                biome.hasPrecipitation(),
                biome.getBaseTemperature(),
                switch (temperatureModifier) {
                    case NONE -> JsonTemperatureModifier.NONE;
                    case FROZEN -> JsonTemperatureModifier.FROZEN;
                },
                downfall
        );
    }

    private static @NonNull JsonBiomeSpecialEffects convertSpecialEffects(@NonNull BiomeSpecialEffects effects) {
        return new JsonBiomeSpecialEffects(
                effects.getFogColor(), effects.getWaterColor(),
                effects.getWaterFogColor(), effects.getSkyColor(),
                effects.getFoliageColorOverride().orElse(null),
                effects.getDryFoliageColorOverride().orElse(null),
                effects.getGrassColorOverride().orElse(null),
                switch (effects.getGrassColorModifier()) {
                    case NONE -> JsonGrassColorModifier.NONE;
                    case DARK_FOREST -> JsonGrassColorModifier.DARK_FOREST;
                    case SWAMP -> JsonGrassColorModifier.SWAMP;
                },
                null, // TODO
                effects.getAmbientLoopSoundEvent().map(BiomeAdapter::convertSoundEvent).orElse(null),
                effects.getAmbientMoodSettings().map(BiomeAdapter::convertMoodSound).orElse(null),
                effects.getAmbientAdditionsSettings().map(BiomeAdapter::convertAdditionsSound).orElse(null),
                effects.getBackgroundMusic().map(BiomeAdapter::convertMusic).orElse(List.of()),
                effects.getBackgroundMusicVolume()
        );
    }

    private static @NonNull JsonAmbientMoodSound convertMoodSound(@NonNull AmbientMoodSettings settings) {
        return new JsonAmbientMoodSound(
                convertSoundEvent(settings.getSoundEvent()),
                settings.getTickDelay(),
                settings.getBlockSearchExtent(),
                settings.getSoundPositionOffset()
        );
    }

    private static @NonNull JsonAdditionsSound convertAdditionsSound(@NonNull AmbientAdditionsSettings settings) {
        return new JsonAdditionsSound(
                convertSoundEvent(settings.getSoundEvent()),
                settings.getTickChance()
        );
    }

    private static @NonNull List<JsonWeighted<JsonMusic>> convertMusic(@NonNull WeightedList<Music> list) {
        List<JsonWeighted<JsonMusic>> convertedList = new ArrayList<>();
        for (Weighted<Music> weighted : list.unwrap()) {
            Music music = weighted.value();
            convertedList.add(
                    new JsonWeighted<>(
                            new JsonMusic(
                                    convertSoundEvent(music.event()),
                                    music.minDelay(),
                                    music.maxDelay(),
                                    music.replaceCurrentMusic()
                            ),
                            weighted.weight()
                    )
            );
        }
        return List.copyOf(convertedList);
    }

    private static @NonNull JsonHolder<JsonSoundEvent> convertSoundEvent(@NonNull Holder<SoundEvent> holder) {
        return holder.unwrap().map(
                key -> new JsonHolder.Reference<>(KeyAdapter.convert(key.location())),
                soundEvent -> new JsonHolder.Direct<>(new JsonSoundEvent(
                        KeyAdapter.convert(soundEvent.location()),
                        soundEvent.fixedRange().orElse(null)
                ))
        );
    }
}