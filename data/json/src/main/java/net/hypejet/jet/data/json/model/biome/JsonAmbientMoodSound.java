package net.hypejet.jet.data.json.model.biome;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An additional ambient sound event that plays in moody situations. Moodiness increases when blocks around the player
 * have both skylight and block light levels set to {@code 0}, and decreases otherwise. The moodiness is calculated
 * once a tick and after reaching a specific value this sound event is played.
 *
 * @param sound a holder of the sound event
 * @param tickDelay a rate at which the moodiness increase
 * @param blockSearchExtent a radius used for block search around the player while calculating moodiness
 * @param offset a distance offset from the player when playing the sound
 * @since 1.0
 */
public record JsonAmbientMoodSound(@NonNull JsonHolder<JsonSoundEvent> sound, int tickDelay,
                                   int blockSearchExtent, double offset) {
    /**
     * Constructs the {@linkplain JsonAmbientMoodSound ambient mood sound}.
     *
     * @param sound a holder of the sound event
     * @param tickDelay a rate at which the moodiness increase
     * @param blockSearchExtent a radius used for block search around the player while calculating moodiness
     * @param offset a distance offset from the player when playing the sound
     * @since 1.0
     */
    public JsonAmbientMoodSound {
        Objects.requireNonNull(sound, "sound");
    }
}