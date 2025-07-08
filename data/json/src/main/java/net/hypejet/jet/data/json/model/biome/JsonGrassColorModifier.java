package net.hypejet.jet.data.json.model.biome;

/**
 * A modifier that affects the final grass color of {@linkplain JsonBiome biomes}.
 *
 * @since 1.0
 */
public enum JsonGrassColorModifier {
    /**
     * A modifier that does not modify the resulting grass color, therefore it is static throughout the biome.
     *
     * @since 1.0
     */
    NONE,
    /**
     * A modifier that makes the resulting grass color darker and less saturated.
     *
     * @since 1.0
     */
    DARK_FOREST,
    /**
     * A modifier that overrides the resulting grass color with fixed values randomly distributed throughout
     * the biome.
     *
     * @since 1.0
     */
    SWAMP
}