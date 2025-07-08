package net.hypejet.jet.data.json.adapters.model;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
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
public final class SoundEventTypeAdapter extends TypeAdapter<JsonSoundEvent> {

    private static final String SOUND_FIELD = "sound";
    private static final String RANGE_FIELD = "range";

    private final Gson gson;

    /**
     * Constructs the {@linkplain SoundEventTypeAdapter sound event type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    public SoundEventTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonSoundEvent value) throws IOException {
        Key sound = value.sound();
        Float range = value.range();

        if (range == null) {
            this.gson.toJson(sound, Key.class, out);
        } else {
            out.beginObject();
            out.name(SOUND_FIELD);
            this.gson.toJson(sound, Key.class, out);
            out.name(RANGE_FIELD);
            out.value(range);
        }
    }

    @Override
    public JsonSoundEvent read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();

            Key sound = null;
            Float range = null;

            while (in.peek() == JsonToken.NAME) {
                String name = in.nextName();
                switch (name) {
                    case SOUND_FIELD -> sound = this.gson.fromJson(in, Key.class);
                    case RANGE_FIELD -> range = (float) in.nextDouble();
                    default -> throw new JsonParseException("Unknown field: " + name);
                }
            }

            if (sound == null) {
                throw new JsonParseException("The sound field has not been specified");
            }

            in.endObject();
            return new JsonSoundEvent(sound, range);
        } else {
            return new JsonSoundEvent(this.gson.fromJson(in, Key.class), null);
        }
    }
}