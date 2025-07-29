package net.hypejet.jet.world.biome.effects;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.sound.SoundEvent;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An additional ambient sound event that plays in moody situations. Moodiness increases when blocks around the player
 * have both skylight and block light levels set to {@code 0}, and decreases otherwise. The moodiness is calculated
 * once a tick and after reaching a specific value this sound event is played.
 *
 * @param soundEvent a holder of the sound event
 * @param tickDelay a rate at which the moodiness increase
 * @param blockSearchExtent a radius used for block search around the player while calculating moodiness
 * @param soundPositionOffset a distance offset from the player when playing the sound
 * @since 1.0
 */
public record AmbientMoodSettings(@NonNull Holder<SoundEvent> soundEvent, int tickDelay,
                                  int blockSearchExtent, double soundPositionOffset) {
    /**
     * Constructs the {@linkplain AmbientMoodSettings ambient mood settings}.
     *
     * @param soundEvent a holder of the sound event
     * @param tickDelay a rate at which the moodiness increase
     * @param blockSearchExtent a radius used for block search around the player while calculating moodiness
     * @param soundPositionOffset a distance offset from the player when playing the sound
     * @since 1.0
     */
    public AmbientMoodSettings {
        Objects.requireNonNull(soundEvent, "sound event");
    }
}