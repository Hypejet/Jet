package net.hypejet.jet.world.coordinate.biome;

import net.hypejet.jet.world.biome.Biome;

/**
 * Represents a position of {@linkplain Biome a biome}.
 *
 * @param x an {@code X} value of the position
 * @param y an {@code y} value of the position
 * @param z an {@code Z} value of the position
 * @since 1.0
 * @see Biome
 */
public record BiomePosition(int x, int y, int z) {}