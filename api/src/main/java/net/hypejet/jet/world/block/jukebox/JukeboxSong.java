package net.hypejet.jet.world.block.jukebox;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.util.range.RangeUtil;
import net.hypejet.jet.world.sound.SoundEvent;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A song that can be played in a Minecraft jukebox block.
 *
 * @param soundEvent the sound event to be played when a disc item representing this song is put into a jukebox
 * @param description a component to display in tooltips of disc items representing this song
 * @param lengthInSeconds the length of the song in seconds
 * @param comparatorOutput the redstone power output of a comparator block when it is placed
 *                         next to a jukebox playing this song
 * @since 1.0
 */
public record JukeboxSong(@NonNull Holder<SoundEvent> soundEvent, @NonNull Component description,
                          float lengthInSeconds, int comparatorOutput) {
    /**
     * Constructs the {@linkplain JukeboxSong jukebox song}.
     *
     * @param soundEvent the sound event to be played when a disc item representing this song is put into a jukebox
     * @param description a component to display in tooltips of disc items representing this song
     * @param lengthInSeconds the length of the song in seconds
     * @param comparatorOutput the redstone power output of a comparator block when it is placed
     *                         next to a jukebox playing this song
     * @since 1.0
     */
    public JukeboxSong {
        Objects.requireNonNull(soundEvent, "sound event");
        Objects.requireNonNull(description, "description");
        RangeUtil.ensureNotNegative(lengthInSeconds);
        RangeUtil.ensureInRange(comparatorOutput, 0, 15);
    }
}