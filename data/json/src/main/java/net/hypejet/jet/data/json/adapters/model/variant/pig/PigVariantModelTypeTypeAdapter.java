package net.hypejet.jet.data.json.adapters.model.variant.pig;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.variant.pig.JsonPigVariant;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter}
 * converting {@linkplain JsonPigVariant.ModelType pig variant model types}.
 *
 * @since 1.0
 * @see JsonPigVariant.ModelType
 * @see TypeAdapter
 */
final class PigVariantModelTypeTypeAdapter extends TypeAdapter<JsonPigVariant.ModelType> {
    /**
     * An instance of the {@linkplain PigVariantModelTypeTypeAdapter pig variant model type type adapter}.
     *
     * @since 1.0
     */
    static final PigVariantModelTypeTypeAdapter INSTANCE = new PigVariantModelTypeTypeAdapter();

    private PigVariantModelTypeTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonPigVariant.ModelType value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonPigVariant.ModelType read(JsonReader in) throws IOException {
        return JsonPigVariant.ModelType.values()[in.nextInt()];
    }
}