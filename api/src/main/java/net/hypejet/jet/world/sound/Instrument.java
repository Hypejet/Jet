package net.hypejet.jet.world.sound;

import net.hypejet.jet.registry.Holder;
import net.hypejet.jet.util.range.RangeUtil;
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
public record Instrument(@NonNull Holder<SoundEvent> soundEvent, float useDuration,
                         float range, @NonNull Component description) {
    /**
     * Constructs the {@link Instrument instrument}.
     *
     * @param soundEvent the sound event
     * @param useDuration the use duration
     * @param range the range
     * @param description a component that is used as a description of this instrument in item tooltips
     * @since 1.0
     */
    public Instrument {
        Objects.requireNonNull(soundEvent, "sound event");
        Objects.requireNonNull(description, "description");
        RangeUtil.ensureNotNegative(useDuration);
        RangeUtil.ensureNotNegative(range);
    }
}