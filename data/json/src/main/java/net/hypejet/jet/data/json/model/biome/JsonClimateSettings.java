package net.hypejet.jet.data.json.model.biome;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Climate settings of {@linkplain JsonBiome a biome}.
 *
 * @param hasPrecipitation whether the biome has a precipitation
 * @param temperature a temperature factor of the biome
 * @param temperatureModifier a modifier that affects the final temperature of the biome
 * @param downfall a downfall factor of the biome, affects foliage and grass color if they are not explicitly set
 * @since 1.0
 */
public record JsonClimateSettings(boolean hasPrecipitation, float temperature,
                                  @NonNull JsonTemperatureModifier temperatureModifier, float downfall) {
    /**
     * Constructs the {@linkplain JsonClimateSettings climate settings}.
     *
     * @param hasPrecipitation whether the biome has a precipitation
     * @param temperature a temperature factor of the biome
     * @param temperatureModifier a modifier that affects the final temperature of the biome
     * @param downfall a downfall factor of the biome, affects foliage and grass color if they are not explicitly set
     * @since 1.0
     */
    public JsonClimateSettings {
        Objects.requireNonNull(temperatureModifier, "temperature modifier");
    }
}