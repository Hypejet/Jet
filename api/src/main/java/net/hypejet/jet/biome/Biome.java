package net.hypejet.jet.biome;

import net.hypejet.jet.biome.effects.BiomeEffectSettings;
import net.hypejet.jet.biome.temperature.TemperatureModifier;
import net.hypejet.jet.registry.Entry;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents a {@linkplain Entry registry entry} of a Minecraft biome.
 *
 * @param key an identifier of the biome
 * @param hasPrecipitation whether the biome has precipitation
 * @param temperature a temperature of the biome
 * @param temperatureModifier a modifier of temperature of the biome, {@code null} if none
 * @param effects effects of the biome
 * @since 1.0
 * @author Codestech
 * @see Entry
 */
public record Biome(@NonNull Key key, boolean hasPrecipitation, float temperature,
                    @Nullable TemperatureModifier temperatureModifier, float downfall,
                    @NonNull BiomeEffectSettings effects) implements Entry {
    /**
     * Constructs the {@linkplain Biome biome}.
     *
     * @param key an identifier of the biome
     * @param hasPrecipitation whether the biome has precipitation
     * @param temperature a temperature of the biome
     * @param temperatureModifier a modifier of temperature of the biome, {@code null} if none
     * @param effects effects of the biome
     * @since 1.0
     */
    public Biome {
        Objects.requireNonNull(key, "The biome identifier must not be null");
        Objects.requireNonNull(effects, "The biome effects must not be null");
    }

    /**
     * Creates a new {@linkplain Builder biome builder}.
     *
     * @return the new biome builder created
     * @since 1.0
     */
    public static @NonNull Builder builder() {
        return new Builder();
    }

    /**
     * Represents a builder of a {@linkplain Biome biome}.
     *
     * @since 1.0
     * @author Codestech
     * @see Biome
     */
    public static final class Builder {

        private Key key;

        private boolean hasPrecipitation;

        private float temperature;
        private float downfall;

        private TemperatureModifier temperatureModifier;
        private BiomeEffectSettings effectSettings;

        private Builder() {}

        /**
         * Sets an {@linkplain Key identifier} that the {@linkplain Biome biome} should have.
         *
         * @param key the identifier
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder key(@NonNull Key key) {
            this.key = Objects.requireNonNull(key, "The key must not be null");
            return this;
        }

        /**
         * Sets whether the {@linkplain Biome biome} should have precipitation.
         *
         * @param hasPrecipitation {@code true} if the biome should have precipitation, {@code false} otherwise
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder hasPrecipitation(boolean hasPrecipitation) {
            this.hasPrecipitation = hasPrecipitation;
            return this;
        }

        /**
         * Sets a temperature that {@linkplain Biome biome} should have.
         *
         * @param temperature the temperature
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder temperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        /**
         * Sets a {@linkplain TemperatureModifier temperature modifier} that the {@linkplain Biome biome} should have.
         *
         * @param temperatureModifier the temperature modifier, {@code null} if none
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder temperatureModifier(@Nullable TemperatureModifier temperatureModifier) {
            this.temperatureModifier = temperatureModifier;
            return this;
        }

        /**
         * Sets a downfall factor that the {@linkplain Biome biome} should have.
         *
         * @param downfall the downfall factor
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder downfall(float downfall) {
            this.downfall = downfall;
            return this;
        }

        /**
         * Sets {@linkplain BiomeEffectSettings biome effect settings} that the {@linkplain Biome biome} should have.
         *
         * @param effectSettings the effect settings
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder effectSettings(@NonNull BiomeEffectSettings effectSettings) {
            this.effectSettings = Objects.requireNonNull(effectSettings, "The effect settings must not be" +
                    " null");
            return this;
        }

        /**
         * Builds a {@linkplain Biome biome} with settings specified in this {@linkplain Builder builder}.
         *
         * @return the biome
         * @since 1.0
         */
        public @NonNull Biome build() {
            return new Biome(Objects.requireNonNull(this.key, "The biome identifier was not specified"),
                    this.hasPrecipitation, this.temperature, this.temperatureModifier, this.downfall,
                    Objects.requireNonNull(this.effectSettings,
                            "The biome effect settings were not specified"));
        }
    }
}