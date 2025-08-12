package net.hypejet.jet.world.biome;

import net.hypejet.jet.world.biome.climate.ClimateSettings;
import net.hypejet.jet.world.biome.effects.SpecialEffects;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Definition of a Minecraft biome.
 *
 * @param climateSettings climate settings of the biome
 * @param specialEffects special effects of the biome
 * @since 1.0
 */
public record Biome(@NonNull ClimateSettings climateSettings, @NonNull SpecialEffects specialEffects) {
    /**
     * Constructs the {@linkplain Biome biome}.
     *
     * @param climateSettings climate settings of the biome
     * @param specialEffects special effects of the biome
     * @since 1.0
     */
    public Biome {
        Objects.requireNonNull(climateSettings, "climate settings");
        Objects.requireNonNull(specialEffects, "special effects");
    }
}