package net.hypejet.jet.world.biome.effects;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.Holder;
import net.hypejet.jet.world.sound.SoundEvent;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents properties of a music to be played in a {@linkplain Biome biome}.
 *
 * @param soundEvent a holder of a sound event of the music
 * @param minDelay a minimum time in ticks since last music finished for this music to be able to play
 * @param maxDelay a maximum time in ticks since last music finished for this music to be able to play
 * @param replaceCurrentMusic whether this music can replace the current one
 * @since 1.0
 * @see Biome
 */
public record Music(@NonNull Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
    /**
     * Constructs the {@linkplain Music music}.
     *
     * @param soundEvent a holder of a sound event of the music
     * @param minDelay a minimum time in ticks since last music finished for this music to be able to play
     * @param maxDelay a maximum time in ticks since last music finished for this music to be able to play
     * @param replaceCurrentMusic whether this music can replace the current one
     * @since 1.0
     */
    public Music {
        Objects.requireNonNull(soundEvent, "sound event");
    }
}