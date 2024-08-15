package net.hypejet.jet.biome.effects.sound;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents an ambient sound effect of a {@linkplain net.hypejet.jet.biome.Biome biome}.
 *
 * @param key an identifier of the sound
 * @param range a range of the sound, {@code null} if a volume should be used to calculate it
 * @since 1.0
 * @author Codestech
 * @see Keyed
 * @see net.hypejet.jet.biome.Biome
 */
public record BiomeAmbientSound(@NonNull Key key, @Nullable Float range) implements Keyed {
    /**
     * Constructs the {@linkplain BiomeAmbientSound biome ambient sound}.
     *
     * @param key an identifier of the sound
     * @param range a range of the sound, {@code null} if a volume should be used to calculate it
     * @since 1.0
     */
    public BiomeAmbientSound {
        Objects.requireNonNull(key, "The sound identifier must not be null");
    }
}