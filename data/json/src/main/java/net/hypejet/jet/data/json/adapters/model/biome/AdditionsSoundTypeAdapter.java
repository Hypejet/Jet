package net.hypejet.jet.data.json.adapters.model.biome;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.model.biome.JsonAdditionsSound;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonAdditionsSound additions sounds}.
 *
 * @since 1.0
 * @see JsonAdditionsSound
 * @see TypeAdapter
 */
final class AdditionsSoundTypeAdapter extends TypeAdapter<JsonAdditionsSound> {

    private static final String SOUND_FIELD = "sound";
    private static final String TICK_CHANCE_FIELD = "tick-chance";

    private final Gson gson;

    /**
     * Constructs the {@linkplain AdditionsSoundTypeAdapter additions sound type adapter}.
     *
     * @param gson the gson object to convert other objects with
     * @since 1.0
     */
    AdditionsSoundTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonAdditionsSound value) throws IOException {
        out.beginObject();

        out.name(SOUND_FIELD);
        this.gson.toJson(value.sound(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.name(TICK_CHANCE_FIELD);
        out.value(value.tickChance());

        out.endObject();
    }

    @Override
    public JsonAdditionsSound read(JsonReader in) throws IOException {
        in.beginObject();

        JsonHolder<JsonSoundEvent> sound = null;
        double tickChance = 0;
        boolean tickChanceInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case SOUND_FIELD -> sound = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case TICK_CHANCE_FIELD -> {
                    tickChance = in.nextDouble();
                    tickChanceInitialized = true;
                }
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (sound == null) {
            throw new JsonParseException("The sound field has not been initialized");
        } else if (!tickChanceInitialized) {
            throw new JsonParseException("The tick chance field has not been initialized");
        }

        return new JsonAdditionsSound(sound, tickChance);
    }
}