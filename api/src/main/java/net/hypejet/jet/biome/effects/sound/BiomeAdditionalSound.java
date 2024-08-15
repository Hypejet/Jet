package net.hypejet.jet.biome.effects.sound;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents an ambient sound effect of a {@linkplain net.hypejet.jet.biome.Biome biome} that has a chance
 * of playing randomly every tick.
 *
 * @param key an identifier of the sound
 * @param tickChance a chance of playing the sound during a tick
 * @since 1.0
 * @author Codestech
 * @see Keyed
 * @see net.hypejet.jet.biome.Biome
 */
public record BiomeAdditionalSound(@NonNull Key key, double tickChance) implements Keyed {
    /**
     * Constructs the {@linkplain BiomeAdditionalSound biome additional sound}.
     *
     * @param key an identifier of the sound
     * @param tickChance a chance of playing the sound during a tick
     * @since 1.0
     */
    public BiomeAdditionalSound {
        Objects.requireNonNull(key, "The sound identifier must not be null");
    }
}