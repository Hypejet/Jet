package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.song.JsonJukeboxSong;
import net.minecraft.world.item.JukeboxSong;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain JukeboxSong jukebox songs} to a Jet data equivalent.
 *
 * @since 1.0
 * @see JukeboxSong
 */
public final class JukeboxSongAdapter {

    private JukeboxSongAdapter() {}

    /**
     * Converts the specified {@linkplain JukeboxSong jukebox song} to a Jet data equivalent.
     *
     * @param song the song to convert
     * @return the converted song
     * @since 1.0
     */
    public static @NonNull JsonJukeboxSong convert(@NonNull JukeboxSong song) {
        return new JsonJukeboxSong(
                HolderAdapter.convertSoundEventHolder(song.soundEvent()),
                ComponentAdapter.convert(song.description()),
                song.lengthInSeconds(),
                song.comparatorOutput()
        );
    }
}