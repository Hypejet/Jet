package net.hypejet.jet.biome.effects.music;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents music properties of a {@linkplain net.hypejet.jet.data.biome.Biome biome}.
 *
 * @param key an identifier of the music sound
 * @param minimumDelay a minimum time in ticks since the last music finished for this music to be able to play
 * @param maximumDelay a maximum time in ticks since the last music finished for this music to be able to play
 * @param replaceCurrentMusic whether this music can replace the current one
 * @since 1.0
 * @author Codestech
 * @see Keyed
 * @see net.hypejet.jet.biome.Biome
 */
public record BiomeMusic(@NonNull Key key, int minimumDelay, int maximumDelay, boolean replaceCurrentMusic)
        implements Keyed {
    /**
     * Constructs the {@linkplain BiomeMusic biome music}.
     *
     * @param key an identifier of the music sound
     * @param minimumDelay a minimum time in ticks since the last music finished for this music to be able to play
     * @param maximumDelay a maximum time in ticks since the last music finished for this music to be able to play
     * @param replaceCurrentMusic whether this music can replace the current one
     * @since 1.0
     */
    public BiomeMusic {
        Objects.requireNonNull(key, "The sound identifier must not be null");
    }
}