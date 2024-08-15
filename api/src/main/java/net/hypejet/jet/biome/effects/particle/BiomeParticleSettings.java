package net.hypejet.jet.biome.effects.particle;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents settings of a Minecraft particle of a {@linkplain net.hypejet.jet.biome.Biome biome}.
 *
 * @param name a name of the particle
 * @param data additional data of the particle, {@code null} if none
 * @param probability a probability of the particle
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.biome.Biome
 */
public record BiomeParticleSettings(@NonNull String name, @Nullable CompoundBinaryTag data, float probability) {
    /**
     * Constructs the {@linkplain BiomeParticleSettings biome particle settings}.
     *
     * @param name a name of the particle
     * @param data additional data of the particle, {@code null} if none
     * @param probability a probability of the particle
     * @since 1.0
     */
    public BiomeParticleSettings {
        Objects.requireNonNull(name, "The name must not be null");
    }
}