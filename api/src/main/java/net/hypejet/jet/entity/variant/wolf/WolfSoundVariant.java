package net.hypejet.jet.entity.variant.wolf;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.sound.SoundEvent;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A variant of sound set used for a Minecraft wolf entity.
 *
 * @param ambientSound holder of a sound event played randomly when a wolf using this sound variant lives
 * @param deathSound holder of a sound event played when a wolf using this sound variant dies
 * @param growlSound holder of a sound event played when a wolf using this sound variant growls
 * @param hurtSound holder of a sound event played when a wolf using this sound variant is damaged
 * @param pantSound holder of a sound event played randomly when a wolf using this sound variant is tamed
 * @param whineSound holder of a sound event played randomly when a wolf using this sound variant has low health
 * @since 1.0
 */
public record WolfSoundVariant(@NonNull Holder<SoundEvent> ambientSound, @NonNull Holder<SoundEvent> deathSound,
                               @NonNull Holder<SoundEvent> growlSound, @NonNull Holder<SoundEvent> hurtSound,
                               @NonNull Holder<SoundEvent> pantSound, @NonNull Holder<SoundEvent> whineSound) {
    /**
     * Constructs the {@linkplain WolfSoundVariant wolf sound variant}.
     *
     * @param ambientSound holder of a sound event played randomly when a wolf using this sound variant lives
     * @param deathSound holder of a sound event played when a wolf using this sound variant dies
     * @param growlSound holder of a sound event played when a wolf using this sound variant growls
     * @param hurtSound holder of a sound event played when a wolf using this sound variant is damaged
     * @param pantSound holder of a sound event played randomly when a wolf using this sound variant is tamed
     * @param whineSound holder of a sound event played randomly when a wolf using this sound variant has low health
     * @since 1.0
     */
    public WolfSoundVariant {
        Objects.requireNonNull(ambientSound, "ambient sound");
        Objects.requireNonNull(deathSound, "death sound");
        Objects.requireNonNull(growlSound, "growl sound");
        Objects.requireNonNull(hurtSound, "hurt sound");
        Objects.requireNonNull(pantSound, "pant sound");
        Objects.requireNonNull(whineSound, "whine sound");
    }
}