package net.hypejet.jet.data.json.adapters.model.instrument;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.model.instrument.JsonInstrument;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonInstrument instruments}.
 *
 * @since 1.0
 * @see JsonInstrument
 * @see TypeAdapter
 */
final class InstrumentTypeAdapter extends TypeAdapter<JsonInstrument> {

    private static final String SOUND_EVENT_FIELD = "sound_event";
    private static final String USE_DURATION_FIELD = "use_duration";
    private static final String RANGE_FIELD = "range";
    private static final String DESCRIPTION_FIELD = "description";

    private final Gson gson;

    /**
     * Constructs the {@linkplain InstrumentTypeAdapter instrument type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    InstrumentTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonInstrument value) throws IOException {
        out.beginObject();

        out.name(SOUND_EVENT_FIELD);
        this.gson.toJson(value.soundEvent(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.name(USE_DURATION_FIELD);
        out.value(value.useDuration());

        out.name(RANGE_FIELD);
        out.value(value.range());

        out.name(DESCRIPTION_FIELD);
        this.gson.toJson(value.description(), Component.class, out);

        out.endObject();
    }

    @Override
    public JsonInstrument read(JsonReader in) throws IOException {
        in.beginObject();

        JsonHolder<JsonSoundEvent> soundEvent = null;
        float useDuration = 0;
        float range = 0;
        Component description = null;

        boolean useDurationInitialized = false;
        boolean rangeInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            switch (in.nextName()) {
                case SOUND_EVENT_FIELD -> soundEvent = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case USE_DURATION_FIELD -> {
                    useDuration = (float) in.nextDouble();
                    useDurationInitialized = true;
                }
                case RANGE_FIELD -> {
                    range = (float) in.nextDouble();
                    rangeInitialized = true;
                }
                case DESCRIPTION_FIELD -> description = this.gson.fromJson(in, Component.class);
            }
        }

        in.endObject();

        if (soundEvent == null) {
            throw new JsonParseException("The sound event has not been specified");
        } else if (!useDurationInitialized) {
            throw new JsonParseException("The use duration has not been specified");
        } else if (!rangeInitialized) {
            throw new JsonParseException("The range has not been specified");
        } else if (description == null) {
            throw new JsonParseException("The description has not been initialized");
        } else {
            return new JsonInstrument(soundEvent, useDuration, range, description);
        }
    }
}