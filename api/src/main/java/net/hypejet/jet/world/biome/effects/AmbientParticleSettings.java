package net.hypejet.jet.world.biome.effects;

import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.particle.Particle;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Settings of a {@linkplain Particle particle} that should be distributed throughout a {@linkplain Biome biome}.
 *
 * @param particle the particle that should be distributed throughout the biome
 * @param probability a probability of spawning the particle where {@code 1}
 *                    is {@code 100%} chance and {@code 0} is {@code 0%} chance
 * @since 1.0
 * @see Particle
 * @see Biome
 */
public record AmbientParticleSettings(@NonNull Particle particle, float probability) {
    /**
     * Constructs the {@linkplain AmbientParticleSettings ambient particle settings}.
     *
     * @param particle the particle that should be distributed throughout the biome
     * @param probability a spawning probability that the particle should have, where
     *                    {@code 1} is {@code 100%} chance and {@code 0} is {@code 0%} chance
     * @since 1.0
     */
    public AmbientParticleSettings {
        Objects.requireNonNull(particle, "particle");
    }
}