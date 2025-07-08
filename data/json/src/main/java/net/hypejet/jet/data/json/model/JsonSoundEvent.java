package net.hypejet.jet.data.json.model;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * A definition of a Minecraft sound related to a specific event.
 *
 * @param sound a key of the sound
 * @param range the fixed range of the sound, {@code null} if the sound should be played with a variable range
 * @since 1.0
 */
public record JsonSoundEvent(@NonNull Key sound, @Nullable Float range) {
    /**
     * Constructs the {@linkplain JsonSoundEvent sound event}.
     *
     * @param sound a key of the sound
     * @param range the fixed range of the sound, {@code null} if the sound should be played with a variable range
     * @since 1.0
     */
    public JsonSoundEvent {
        Objects.requireNonNull(sound, "sound");
    }
}