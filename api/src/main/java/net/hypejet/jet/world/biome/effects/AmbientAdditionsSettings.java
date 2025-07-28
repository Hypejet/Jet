package net.hypejet.jet.world.biome.effects;

import net.hypejet.jet.registry.Holder;
import net.hypejet.jet.world.sound.SoundEvent;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An additional ambient sound event that has a chance of playing randomly every tick.
 *
 * @param soundEvent a holder of the sound event
 * @param tickChance a chance of playing the sound during the tick
 * @since 1.0
 */
public record AmbientAdditionsSettings(@NonNull Holder<SoundEvent> soundEvent, double tickChance) {
    /**
     * Constructs the {@linkplain AmbientAdditionsSettings ambient additions settings}.
     *
     * @param soundEvent a holder of the sound event
     * @param tickChance a chance of playing the sound during the tick
     * @since 1.0
     */
    public AmbientAdditionsSettings {
        Objects.requireNonNull(soundEvent, "sound event");
    }
}