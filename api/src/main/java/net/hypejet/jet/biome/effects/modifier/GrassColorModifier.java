package net.hypejet.jet.biome.effects.modifier;

/**
 * Represents a modifier of a Minecraft grass color.
 *
 * @since 1.0
 * @author Codestech
 */
public enum GrassColorModifier {
    /**
     * Represents a color modifier, which does not do any additional modifications of grass colors.
     *
     * @since 1.0
     */
    NONE,
    /**
     * Represents a color modifier, which makes grass colors darker and less saturated.
     *
     * @since 1.0
     */
    DARK_FOREST,
    /**
     * Represents a color modifier, which overrides grass colors with two fixed values randomly distributed
     * throughout a {@linkplain net.hypejet.jet.data.biome.Biome biome}.
     *
     * @since 1.0
     */
    SWAMP
}