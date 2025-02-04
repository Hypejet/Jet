package net.hypejet.jet.world.coordinate;

/**
 * Represents a position of {@linkplain net.hypejet.jet.data.model.api.registries.biome.Biome a biome}.
 *
 * @param x an {@code X} value of the position
 * @param y an {@code y} value of the position
 * @param z an {@code Z} value of the position
 * @since 1.0
 * @see net.hypejet.jet.data.model.api.registries.biome.Biome
 */
public record BiomePosition(int x, int y, int z) {}