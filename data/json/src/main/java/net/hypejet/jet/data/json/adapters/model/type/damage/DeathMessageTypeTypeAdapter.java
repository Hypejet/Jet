package net.hypejet.jet.data.json.adapters.model.type.damage;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.type.damage.JsonDeathMessageType;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonDeathMessageType death message types}.
 *
 * @since 1.0
 * @see JsonDeathMessageType
 * @see TypeAdapter
 */
final class DeathMessageTypeTypeAdapter extends TypeAdapter<JsonDeathMessageType> {
    /**
     * An instance of the {@linkplain DeathMessageTypeTypeAdapter death message type type adapter}.
     *
     * @since 1.0
     */
    static final DeathMessageTypeTypeAdapter INSTANCE = new DeathMessageTypeTypeAdapter();

    private DeathMessageTypeTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonDeathMessageType value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonDeathMessageType read(JsonReader in) throws IOException {
        return JsonDeathMessageType.values()[in.nextInt()];
    }
}