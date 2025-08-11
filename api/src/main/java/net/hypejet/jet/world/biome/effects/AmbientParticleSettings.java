package net.hypejet.jet.world.biome.effects;

import net.hypejet.jet.world.biome.Biome;
import net.kyori.adventure.nbt.BinaryTag;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Settings of a particle that should be distributed throughout a {@linkplain Biome biome}.
 *
 * @param options options of the particle
 * @param probability a probability of spawning the particle where {@code 1} is {@code 100%} chance and {@code 0}
 *                    is {@code 0%} chance
 * @since 1.0
 * @see Biome
 */
// TODO: Implement particle options and replace the type of "options" field with it
public record AmbientParticleSettings(@NonNull BinaryTag options, float probability) {
    /**
     * Constructs the {@linkplain AmbientParticleSettings ambient particle settings}.
     *
     * @param options options of the particle
     * @param probability a probability of spawning the particle where {@code 1} is {@code 100%} chance and {@code 0}
     *                    is {@code 0%} chance
     * @since 1.0
     */
    public AmbientParticleSettings {
        Objects.requireNonNull(options, "options");
    }
}