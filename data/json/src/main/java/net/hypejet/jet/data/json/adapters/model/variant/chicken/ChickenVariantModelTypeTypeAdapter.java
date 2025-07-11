package net.hypejet.jet.data.json.adapters.model.variant.chicken;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.variant.chicken.JsonChickenVariant;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter}
 * of {@linkplain JsonChickenVariant.ModelType chicken variant model types}.
 *
 * @since 1.0
 * @see JsonChickenVariant.ModelType
 * @see TypeAdapter
 */
final class ChickenVariantModelTypeTypeAdapter extends TypeAdapter<JsonChickenVariant.ModelType> {
    /**
     * An instance of the {@linkplain ChickenVariantModelTypeTypeAdapter chicken variant model type type adapter}.
     *
     * @since 1.0
     */
    static final ChickenVariantModelTypeTypeAdapter INSTANCE = new ChickenVariantModelTypeTypeAdapter();

    private ChickenVariantModelTypeTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonChickenVariant.ModelType value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonChickenVariant.ModelType read(JsonReader in) throws IOException {
        return JsonChickenVariant.ModelType.values()[in.nextInt()];
    }
}