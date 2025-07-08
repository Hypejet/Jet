package net.hypejet.jet.data.json.adapters.model.biome;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.biome.JsonTemperatureModifier;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonTemperatureModifier temperature modifiers}.
 *
 * @since 1.0
 * @see JsonTemperatureModifier
 * @see TypeAdapter
 */
final class TemperatureModifierTypeAdapter extends TypeAdapter<JsonTemperatureModifier> {
    /**
     * An instance of the {@linkplain TemperatureModifierTypeAdapter temperature modifier type adapter}.
     *
     * @since 1.0
     */
    static final TemperatureModifierTypeAdapter INSTANCE = new TemperatureModifierTypeAdapter();

    private TemperatureModifierTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonTemperatureModifier value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonTemperatureModifier read(JsonReader in) throws IOException {
        return JsonTemperatureModifier.values()[in.nextInt()];
    }
}