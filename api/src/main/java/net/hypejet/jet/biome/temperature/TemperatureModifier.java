package net.hypejet.jet.biome.temperature;

/**
 * Represents a modifier of temperature of a {@linkplain net.hypejet.jet.biome.Biome biome}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.biome.Biome
 */
public enum TemperatureModifier {
    /**
     * Represents a temperature modifier, which does not do any additional modifications of the temperature.
     *
     * @since 1.0
     */
    NONE,
    /**
     * Represents a temperature modifier, which randomly distributes pockets of warm temperature throughout the
     * {@linkplain net.hypejet.jet.biome.Biome biome}.
     *
     * @since 1.0
     */
    FROZEN
}