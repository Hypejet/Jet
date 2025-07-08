package net.hypejet.jet.data.json.adapters.model.biome;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.biome.JsonAdditionsSound;
import net.hypejet.jet.data.json.model.biome.JsonAmbientMoodSound;
import net.hypejet.jet.data.json.model.biome.JsonAmbientParticleSettings;
import net.hypejet.jet.data.json.model.biome.JsonBiome;
import net.hypejet.jet.data.json.model.biome.JsonBiomeSpecialEffects;
import net.hypejet.jet.data.json.model.biome.JsonClimateSettings;
import net.hypejet.jet.data.json.model.biome.JsonGrassColorModifier;
import net.hypejet.jet.data.json.model.biome.JsonMusic;
import net.hypejet.jet.data.json.model.biome.JsonTemperatureModifier;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting types related to {@linkplain JsonBiome biomes}.
 *
 * @since 1.0
 * @see TypeAdapterFactory
 */
public final class BiomeTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain BiomeTypeAdapterFactory biome type adapter factory}.
     *
     * @since 1.0
     */
    public static final BiomeTypeAdapterFactory INSTANCE = new BiomeTypeAdapterFactory();

    private BiomeTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<?> rawType = type.getRawType();
        if (JsonAdditionsSound.class.isAssignableFrom(rawType)) {
            return new AdditionsSoundTypeAdapter(gson);
        } else if (JsonAmbientMoodSound.class.isAssignableFrom(rawType)) {
            return new AmbientMoodSoundTypeAdapter(gson);
        } else if (JsonAmbientParticleSettings.class.isAssignableFrom(rawType)) {
            return new AmbientParticleSettingsTypeAdapter();
        } else if (JsonBiomeSpecialEffects.class.isAssignableFrom(rawType)) {
            return new BiomeSpecialEffectsTypeAdapter(gson);
        } else if (JsonBiome.class.isAssignableFrom(rawType)) {
            return new BiomeTypeAdapter(gson);
        } else if (JsonClimateSettings.class.isAssignableFrom(rawType)) {
            return new ClimateSettingsTypeAdapter(gson);
        } else if (JsonGrassColorModifier.class.isAssignableFrom(rawType)) {
            return GrassColorModifierTypeAdapter.INSTANCE;
        } else if (JsonMusic.class.isAssignableFrom(rawType)) {
            return new MusicTypeAdapter(gson);
        } else if (JsonTemperatureModifier.class.isAssignableFrom(rawType)) {
            return TemperatureModifierTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}