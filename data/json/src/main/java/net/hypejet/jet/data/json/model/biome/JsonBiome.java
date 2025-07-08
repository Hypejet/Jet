package net.hypejet.jet.data.json.model.biome;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents definition of a Minecraft biome.
 *
 * @param climateSettings climate settings of the biome
 * @param biomeSpecialEffects special effects of the biome
 * @since 1.0
 */
public record JsonBiome(@NonNull JsonClimateSettings climateSettings,
                        @NonNull JsonBiomeSpecialEffects biomeSpecialEffects) {
    /**
     * Constructs the {@linkplain JsonBiome biome}.
     *
     * @param climateSettings climate settings of the biome
     * @param biomeSpecialEffects special effects of the biome
     * @since 1.0
     */
    public JsonBiome {
        Objects.requireNonNull(climateSettings, "climate settings");
        Objects.requireNonNull(biomeSpecialEffects, "biome special effects");
    }
}