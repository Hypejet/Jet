package net.hypejet.jet.data.json.adapters.model.biome;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.biome.JsonGrassColorModifier;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonGrassColorModifier grass color modifiers}.
 *
 * @since 1.0
 * @see JsonGrassColorModifier
 * @see TypeAdapter
 */
final class GrassColorModifierTypeAdapter extends TypeAdapter<JsonGrassColorModifier> {
    /**
     * An instance of the {@linkplain GrassColorModifierTypeAdapter grass color modifier adapter}.
     *
     * @since 1.0
     */
    static final GrassColorModifierTypeAdapter INSTANCE = new GrassColorModifierTypeAdapter();

    private GrassColorModifierTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonGrassColorModifier value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonGrassColorModifier read(JsonReader in) throws IOException {
        return JsonGrassColorModifier.values()[in.nextInt()];
    }
}