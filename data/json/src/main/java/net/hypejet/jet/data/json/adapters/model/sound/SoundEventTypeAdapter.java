package net.hypejet.jet.data.json.adapters.model.sound;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.sound.JsonSoundEvent;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonSoundEvent sound events}.
 *
 * @since 1.0
 * @see JsonSoundEvent
 * @see TypeAdapter
 */
final class SoundEventTypeAdapter extends TypeAdapter<JsonSoundEvent> {

    private static final String SOUND_FIELD = "sound";
    private static final String RANGE_FIELD = "range";

    private final Gson gson;

    /**
     * Constructs the {@linkplain SoundEventTypeAdapter sound event type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    SoundEventTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonSoundEvent value) throws IOException {
        out.beginObject();

        out.name(SOUND_FIELD);
        this.gson.toJson(value.sound(), Key.class, out);

        Float range = value.range();
        if (range != null) {
            out.name(RANGE_FIELD);
            out.value(range);
        }

        out.endObject();
    }

    @Override
    public JsonSoundEvent read(JsonReader in) throws IOException {
        in.beginObject();

        Key sound = null;
        Float range = null;

        while (in.peek() == JsonToken.NAME) {
            switch (in.nextName()) {
                case SOUND_FIELD -> sound = this.gson.fromJson(in, Key.class);
                case RANGE_FIELD -> range = (float) in.nextDouble();
            }
        }

        in.endObject();

        if (sound == null) {
            throw new JsonParseException("The sound has not been specified");
        } else {
            return new JsonSoundEvent(sound, range);
        }
    }
}