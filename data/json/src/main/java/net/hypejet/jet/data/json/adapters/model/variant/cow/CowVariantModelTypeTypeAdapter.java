package net.hypejet.jet.data.json.adapters.model.variant.cow;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.variant.cow.JsonCowVariant;
import net.hypejet.jet.data.json.model.variant.pig.JsonPigVariant;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonCowVariant.ModelType cow variant model types}.
 *
 * @since 1.0
 * @see JsonPigVariant.ModelType
 * @see TypeAdapter
 */
final class CowVariantModelTypeTypeAdapter extends TypeAdapter<JsonCowVariant.ModelType> {
    /**
     * An instance of the {@linkplain CowVariantModelTypeTypeAdapter cow variant model type type adapter}.
     *
     * @since 1.0
     */
    static final CowVariantModelTypeTypeAdapter INSTANCE = new CowVariantModelTypeTypeAdapter();

    private CowVariantModelTypeTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonCowVariant.ModelType value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonCowVariant.ModelType read(JsonReader in) throws IOException {
        return JsonCowVariant.ModelType.values()[in.nextInt()];
    }
}