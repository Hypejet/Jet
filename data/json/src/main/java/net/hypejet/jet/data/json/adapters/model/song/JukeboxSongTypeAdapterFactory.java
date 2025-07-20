package net.hypejet.jet.data.json.adapters.model.song;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.song.JsonJukeboxSong;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonJukeboxSong jukebox songs}.
 *
 * @since 1.0
 * @see JsonJukeboxSong
 * @see TypeAdapterFactory
 */
public final class JukeboxSongTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain JukeboxSongTypeAdapterFactory jukebox song type adapter factory}.
     *
     * @since 1.0
     */
    public static final JukeboxSongTypeAdapterFactory INSTANCE = new JukeboxSongTypeAdapterFactory();

    private JukeboxSongTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (JsonJukeboxSong.class.isAssignableFrom(type.getRawType())) {
            return new JukeboxSongTypeAdapter(gson);
        } else {
            return null;
        }
    }
}