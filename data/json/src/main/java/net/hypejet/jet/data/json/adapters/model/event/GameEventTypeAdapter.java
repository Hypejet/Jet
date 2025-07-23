package net.hypejet.jet.data.json.adapters.model.event;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.event.JsonGameEvent;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonGameEvent game events}.
 *
 * @since 1.0
 * @see JsonGameEvent
 * @see TypeAdapter
 */
final class GameEventTypeAdapter extends TypeAdapter<JsonGameEvent> {
    /**
     * An instance of the {@linkplain GameEventTypeAdapter game event type adapter}.
     *
     * @since 1.0
     */
    static final GameEventTypeAdapter INSTANCE = new GameEventTypeAdapter();

    private GameEventTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonGameEvent value) throws IOException {
        out.value(value.notificationRadius());
    }

    @Override
    public JsonGameEvent read(JsonReader in) throws IOException {
        return new JsonGameEvent(in.nextInt());
    }
}