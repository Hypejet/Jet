package net.hypejet.jet.data.json.adapters.model.variant.chicken;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.variant.chicken.JsonChickenVariant;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonChickenVariant chicken variants}.
 *
 * @since 1.0
 * @see JsonChickenVariant
 * @see TypeAdapter
 */
final class ChickenVariantTypeAdapter extends TypeAdapter<JsonChickenVariant> {

    private static final String MODEL_TYPE_FIELD = "model-type";
    private static final String ASSET_FIELD = "asset";

    private final Gson gson;

    /**
     * Constructs the {@linkplain ChickenVariantTypeAdapter chicken variant type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    ChickenVariantTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonChickenVariant value) throws IOException {
        out.beginObject();

        out.name(MODEL_TYPE_FIELD);
        this.gson.toJson(value.modelType(), JsonChickenVariant.ModelType.class, out);

        out.name(ASSET_FIELD);
        this.gson.toJson(value.asset(), Key.class, out);

        out.endObject();
    }

    @Override
    public JsonChickenVariant read(JsonReader in) throws IOException {
        in.beginObject();

        JsonChickenVariant.ModelType modelType = null;
        Key asset = null;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case MODEL_TYPE_FIELD -> modelType = this.gson.fromJson(in, JsonChickenVariant.ModelType.class);
                case ASSET_FIELD -> asset = this.gson.fromJson(in, Key.class);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (modelType == null) {
            throw new JsonParseException("The model field has not been specified");
        } else if (asset == null) {
            throw new JsonParseException("The asset has not been specified");
        }

        return new JsonChickenVariant(modelType, asset);
    }
}