package net.hypejet.jet.data.json.model.biome;

/**
 * A modifier that affects the final temperature of a {@linkplain JsonBiome biomes}.
 *
 * @since 1.0
 */
public enum JsonTemperatureModifier {
    /**
     * A temperature modifier that does not affect the resulting temperature, therefore it is static throughout
     * the biome (aside from variations depending on height).
     *
     * @since 1.0
     */
    NONE,
    /**
     * A temperature modifier which randomly distributes pockets of a warm temperature, making some places
     * have temperature high enough to rain.
     *
     * @since 1.0
     */
    FROZEN
}