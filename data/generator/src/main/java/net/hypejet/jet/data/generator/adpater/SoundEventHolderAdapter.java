package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain SoundEvent sound event} {@linkplain Holder holders}
 * to a Jet data equivalent.
 *
 * @since 1.0
 * @see SoundEvent
 * @see Holder
 */
public final class SoundEventHolderAdapter {

    private SoundEventHolderAdapter() {}

    /**
     * Converts the specified {@linkplain SoundEvent sound event} {@linkplain Holder holder} to a Jet data equivalent.
     *
     * @param holder the sound event holder to convert
     * @return the converted sound event holder
     * @since 1.0
     */
    public static @NonNull JsonHolder<JsonSoundEvent> convert(@NonNull Holder<SoundEvent> holder) {
        return holder.unwrap().map(
                key -> new JsonHolder.Reference<>(KeyAdapter.convert(key.location())),
                soundEvent -> new JsonHolder.Direct<>(new JsonSoundEvent(
                        KeyAdapter.convert(soundEvent.location()),
                        soundEvent.fixedRange().orElse(null)
                ))
        );
    }
}