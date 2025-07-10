package net.hypejet.jet.data.json.model.variant.wolf;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of sound set used for a Minecraft wolf.
 *
 * @param ambientSound holder of a sound event played randomly when a wolf using this sound variant lives
 * @param deathSound holder of a sound event played when a wolf using this sound variant dies
 * @param growlSound holder of a sound event played when a wolf using this sound variant growls
 * @param hurtSound holder of a sound event played when a wolf using this sound variant is damaged
 * @param pantSound holder of a sound event played randomly when a wolf using this sound variant is tamed
 * @param whineSound holder of a sound event played randomly when a wolf using this sound variant has low health
 * @since 1.0
 */
public record JsonWolfSoundVariant(
        @NonNull JsonHolder<JsonSoundEvent> ambientSound, @NonNull JsonHolder<JsonSoundEvent> deathSound,
        @NonNull JsonHolder<JsonSoundEvent> growlSound, @NonNull JsonHolder<JsonSoundEvent> hurtSound,
        @NonNull JsonHolder<JsonSoundEvent> pantSound, @NonNull JsonHolder<JsonSoundEvent> whineSound
) {
    /**
     * Constructs the {@linkplain JsonWolfSoundVariant wolf sound variant}.
     *
     * @param ambientSound holder of a sound event played randomly when a wolf using this sound variant lives
     * @param deathSound holder of a sound event played when a wolf using this sound variant dies
     * @param growlSound holder of a sound event played when a wolf using this sound variant growls
     * @param hurtSound holder of a sound event played when a wolf using this sound variant is damaged
     * @param pantSound holder of a sound event played randomly when a wolf using this sound variant is tamed
     * @param whineSound holder of a sound event played randomly when a wolf using this sound variant has low health
     * @since 1.0
     */
    public JsonWolfSoundVariant {
        Objects.requireNonNull(ambientSound, "ambient sound");
        Objects.requireNonNull(deathSound, "death sound");
        Objects.requireNonNull(growlSound, "growl sound");
        Objects.requireNonNull(hurtSound, "hurt sound");
        Objects.requireNonNull(pantSound, "pant sound");
        Objects.requireNonNull(whineSound, "whine sound");
    }
}