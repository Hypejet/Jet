package net.hypejet.jet.data.json.model.biome;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents properties of a music to be played in a {@linkplain JsonBiome biome}.
 *
 * @param sound a holder of a sound event of the music
 * @param minDelay a minimum time in ticks since last music finished for this music to be able to play
 * @param maxDelay a maximum time in ticks since last music finished for this music to be able to play
 * @param replaceCurrentMusic whether this music can replace the current one
 * @since 1.0
 * @see JsonBiome
 */
public record JsonMusic(@NonNull JsonHolder<JsonSoundEvent> sound, int minDelay,
                        int maxDelay, boolean replaceCurrentMusic) {
    /**
     * Constructs the {@linkplain JsonMusic music}.
     *
     * @param sound a holder of a sound event of the music
     * @param minDelay a minimum time in ticks since last music finished for this music to be able to play
     * @param maxDelay a maximum time in ticks since last music finished for this music to be able to play
     * @param replaceCurrentMusic whether this music can replace the current one
     * @since 1.0
     */
    public JsonMusic {
        Objects.requireNonNull(sound, "sound");
    }
}