package net.hypejet.jet.data.json.model.song;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.util.RangeUtil;
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
public record JsonJukeboxSong(@NonNull JsonHolder<JsonSoundEvent> soundEvent, @NonNull Component description,
                              float lengthInSeconds, int comparatorOutput) {
    /**
     * Constructs the {@linkplain JsonJukeboxSong jukebox song}.
     *
     * @param soundEvent the sound event to be played when a disc item representing this song is put into a jukebox
     * @param description a component to display in tooltips of disc items representing this song
     * @param lengthInSeconds the length of the song in seconds
     * @param comparatorOutput the redstone power output of a comparator block when it is placed
     *                         next to a jukebox playing this song
     * @since 1.0
     */
    public JsonJukeboxSong {
        Objects.requireNonNull(soundEvent, "sound event");
        Objects.requireNonNull(description, "description");
        RangeUtil.ensureInRange(0, 15, comparatorOutput);
    }
}