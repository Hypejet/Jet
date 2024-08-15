package net.hypejet.jet.biome.effects.sound;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a mood sound effect settings of a {@linkplain net.hypejet.jet.biome.Biome biome}.
 *
 * <p>The sound plays in the direction of the selected block during moodiness calculation, and is magnified by
 * the offset.</p>
 *
 * @param key an identifier of the sound
 * @param tickDelay a minimum time between plays and a rate, at which the moodiness increase
 * @param blockSearchExtent a radius used for the block search around player during the moodiness calculation
 * @param offset a distance offset from the player during playing the sound
 * @since 1.0
 * @see Keyed
 * @author Codestech
 */
public record BiomeMoodSound(@NonNull Key key, int tickDelay, int blockSearchExtent, double offset) implements Keyed {
    /**
     * Constructs the {@linkplain BiomeMoodSound biome mood sound}.
     *
     * @param key an identifier of the sound
     * @param tickDelay a minimum time between plays and a rate, at which the moodiness increase
     * @param blockSearchExtent a radius used for the block search around player during the moodiness calculation
     * @param offset a distance offset from the player during playing the sound
     * @since 1.0
     */
    public BiomeMoodSound {
        Objects.requireNonNull(key, "The sound identifier must not be null");
    }
}