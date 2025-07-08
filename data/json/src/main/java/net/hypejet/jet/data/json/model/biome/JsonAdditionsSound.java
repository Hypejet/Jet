package net.hypejet.jet.data.json.model.biome;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An additional ambient sound event that has a chance of playing randomly every tick.
 *
 * @param sound a holder of the sound event
 * @param tickChance a chance of playing the sound during the tick
 * @since 1.0
 */
public record JsonAdditionsSound(@NonNull JsonHolder<JsonSoundEvent> sound, double tickChance) {
    /**
     * Constructs the {@linkplain JsonAdditionsSound additions sound}.
     *
     * @param sound a holder of the sound event
     * @param tickChance a chance of playing the sound during the tick
     * @since 1.0
     */
    public JsonAdditionsSound {
        Objects.requireNonNull(sound, "sound");
    }
}