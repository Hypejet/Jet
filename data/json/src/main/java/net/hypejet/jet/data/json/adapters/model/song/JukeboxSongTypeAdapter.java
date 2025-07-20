package net.hypejet.jet.data.json.adapters.model.song;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.model.song.JsonJukeboxSong;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonJukeboxSong jukebox songs}.
 *
 * @since 1.0
 * @see JsonJukeboxSong
 * @see TypeAdapter
 */
final class JukeboxSongTypeAdapter extends TypeAdapter<JsonJukeboxSong> {

    private static final String SOUND_EVENT_FIELD = "sound-event";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String LENGTH_IN_SECONDS_FIELD = "length-in-seconds";
    private static final String COMPARATOR_OUTPUT_FIELD = "comparator-output";

    private final Gson gson;

    /**
     * Constructs the {@linkplain JukeboxSongTypeAdapter jukebox song type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    JukeboxSongTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonJukeboxSong value) throws IOException {
        out.beginObject();

        out.name(SOUND_EVENT_FIELD);
        this.gson.toJson(value.soundEvent(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.name(DESCRIPTION_FIELD);
        this.gson.toJson(value.description(), Component.class, out);

        out.name(LENGTH_IN_SECONDS_FIELD);
        out.value(value.lengthInSeconds());

        out.name(COMPARATOR_OUTPUT_FIELD);
        out.value(value.comparatorOutput());

        out.endObject();
    }

    @Override
    public JsonJukeboxSong read(JsonReader in) throws IOException {
        in.beginObject();

        JsonHolder<JsonSoundEvent> soundEvent = null;
        Component description = null;
        int lengthInSeconds = 0;
        int comparatorOutput = 0;

        boolean lengthInitialized = false;
        boolean comparatorOutputInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case SOUND_EVENT_FIELD -> soundEvent = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case DESCRIPTION_FIELD -> description = this.gson.fromJson(in, Component.class);
                case LENGTH_IN_SECONDS_FIELD -> {
                    lengthInSeconds = in.nextInt();
                    lengthInitialized = true;
                }
                case COMPARATOR_OUTPUT_FIELD -> {
                    comparatorOutput = in.nextInt();
                    comparatorOutputInitialized = true;
                }
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (soundEvent == null) {
            throw new JsonParseException("The sound event has not been specified");
        } else if (description == null) {
            throw new JsonParseException("The description has not been specified");
        } else if (!lengthInitialized) {
            throw new JsonParseException("The length has not been specified");
        } else if (!comparatorOutputInitialized) {
            throw new JsonParseException("The comparator output has not been specified");
        } else {
            return new JsonJukeboxSong(soundEvent, description, lengthInSeconds, comparatorOutput);
        }
    }

}