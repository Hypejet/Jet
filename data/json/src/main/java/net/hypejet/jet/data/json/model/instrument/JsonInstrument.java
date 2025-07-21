package net.hypejet.jet.data.json.model.instrument;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A Minecraft sound event with its corresponding range and use duration.
 *
 * @param soundEvent the sound event
 * @param useDuration the use duration
 * @param range the range
 * @param description a component that is used as a description of this instrument in item tooltips
 * @since 1.0
 */
public record JsonInstrument(@NonNull JsonHolder<JsonSoundEvent> soundEvent, float useDuration,
                             float range, @NonNull Component description) {
    /**
     * Constructs the {@linkplain JsonInstrument instrument}.
     *
     * @param soundEvent the sound event
     * @param useDuration the use duration
     * @param range the range
     * @param description a component that is used as a description of this instrument in item tooltips
     * @since 1.0
     */
    public JsonInstrument {
        Objects.requireNonNull(soundEvent, "sound event");
        Objects.requireNonNull(description, "description");
    }
}