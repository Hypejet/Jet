package net.hypejet.jet.data.generator.adapter;

import net.hypejet.jet.data.json.model.sound.JsonSoundEvent;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain SoundEvent sound events} to a Jet data equivalent.
 *
 * @since 1.0
 * @see SoundEvent
 */
public final class SoundEventAdapter {

    private SoundEventAdapter() {}

    /**
     * Converts the specified {@linkplain SoundEvent sound event} to a Jet data equivalent.
     *
     * @param event the sound event to convert
     * @return the converted sound event
     * @since 1.0
     */
    public static @NonNull JsonSoundEvent convert(@NonNull SoundEvent event) {
        return new JsonSoundEvent(KeyAdapter.convert(event.location()), event.fixedRange().orElse(null));
    }
}
