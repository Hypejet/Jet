package net.hypejet.jet.data.json.adapters.model.biome;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.model.biome.JsonMusic;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonMusic music}.
 *
 * @since 1.0
 * @see JsonMusic
 * @see TypeAdapter
 */
final class MusicTypeAdapter extends TypeAdapter<JsonMusic> {

    private static final String SOUND_FIELD = "sound";
    private static final String MIN_DELAY_FIELD = "min-delay";
    private static final String MAX_DELAY_FIELD = "max-delay";
    private static final String REPLACE_CURRENT_MUSIC_FIELD = "replace-current-music";

    private final Gson gson;

    /**
     * Constructs the {@linkplain MusicTypeAdapter music type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    MusicTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonMusic value) throws IOException {
        out.beginObject();

        out.name(SOUND_FIELD);
        this.gson.toJson(value.sound(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.name(MIN_DELAY_FIELD);
        out.value(value.minDelay());

        out.name(MAX_DELAY_FIELD);
        out.value(value.maxDelay());

        out.name(REPLACE_CURRENT_MUSIC_FIELD);
        out.value(value.replaceCurrentMusic());

        out.endObject();
    }

    @Override
    public JsonMusic read(JsonReader in) throws IOException {
        in.beginObject();

        JsonHolder<JsonSoundEvent> sound = null;
        int minDelay = 0;
        int maxDelay = 0;
        boolean replaceCurrentMusic = false;

        boolean minDelayInitialized = false;
        boolean maxDelayInitialized = false;
        boolean replaceCurrentMusicInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case SOUND_FIELD -> sound = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case MIN_DELAY_FIELD -> {
                    minDelay = in.nextInt();
                    minDelayInitialized = true;
                }
                case MAX_DELAY_FIELD -> {
                    maxDelay = in.nextInt();
                    maxDelayInitialized = true;
                }
                case REPLACE_CURRENT_MUSIC_FIELD -> {
                    replaceCurrentMusic = true;
                    replaceCurrentMusicInitialized = true;
                }
                default -> throw new JsonParseException("Unknown field:" + name);
            }
        }

        in.endObject();

        if (sound == null) {
            throw new JsonParseException("The sound field has not been specified");
        } else if (!minDelayInitialized) {
            throw new JsonParseException("The min delay field has not been specified");
        } else if (!maxDelayInitialized) {
            throw new JsonParseException("The max delay field has not been specified");
        } else if (!replaceCurrentMusicInitialized) {
            throw new JsonParseException("The replace current music field has not been specified");
        }

        return new JsonMusic(sound, minDelay, maxDelay, replaceCurrentMusic);
    }
}