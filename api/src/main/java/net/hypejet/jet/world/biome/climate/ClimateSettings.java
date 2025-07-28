package net.hypejet.jet.world.biome.climate;

import net.hypejet.jet.world.biome.Biome;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Climate settings of a {@linkplain Biome biome}.
 *
 * @param hasPrecipitation whether the biome has precipitation
 * @param temperature a temperature factor of the biome
 * @param temperatureModifier a modifier affecting the final temperature of the biome
 * @param downfall a downfall factor of the biome, which affects foliage and grass color if they are not explictly set
 * @since 1.0
 * @see ClimateSettings
 */
public record ClimateSettings(
        boolean hasPrecipitation, float temperature,
        @NonNull TemperatureModifier temperatureModifier, float downfall
) {
    /**
     * Constructs the {@linkplain ClimateSettings climate settings implementation}.
     *
     * @param hasPrecipitation whether the biome has precipitation
     * @param temperature a temperature factor of the biome
     * @param temperatureModifier a modifier affecting the final temperature of the biome
     * @param downfall a downfall factor of the biome, which affects foliage and grass color if they
     *                 are not explicitly set
     * @since 1.0
     */
    public ClimateSettings {
        Objects.requireNonNull(temperatureModifier, "temperature modifier");
    }
}