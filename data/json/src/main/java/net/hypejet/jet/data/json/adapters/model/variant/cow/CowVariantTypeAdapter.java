package net.hypejet.jet.data.json.adapters.model.variant.cow;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.variant.cow.JsonCowVariant;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonCowVariant cow variants}.
 *
 * @since 1.0
 * @see JsonCowVariant
 * @see TypeAdapter
 */
final class CowVariantTypeAdapter extends TypeAdapter<JsonCowVariant> {

    private static final String MODEL_TYPE_FIELD = "model";
    private static final String ASSET_FIELD = "asset";

    private final Gson gson;

    /**
     * Constructs the {@linkplain CowVariantTypeAdapter cow variant type adapter}.
     *
     * @param gson a gson object to serialize other objects with
     * @since 1.0
     */
    CowVariantTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonCowVariant value) throws IOException {
        out.beginObject();

        out.name(MODEL_TYPE_FIELD);
        this.gson.toJson(value.modelType(), JsonCowVariant.ModelType.class, out);

        out.name(ASSET_FIELD);
        this.gson.toJson(value.asset(), Key.class, out);

        out.endObject();
    }

    @Override
    public JsonCowVariant read(JsonReader in) throws IOException {
        in.beginObject();

        JsonCowVariant.ModelType modelType = null;
        Key asset = null;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case MODEL_TYPE_FIELD -> modelType = this.gson.fromJson(in, JsonCowVariant.ModelType.class);
                case ASSET_FIELD -> asset = this.gson.fromJson(in, Key.class);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (modelType == null) {
            throw new JsonParseException("The model type has not been specified");
        } else if (asset == null) {
            throw new JsonParseException("The asset has not been specified");
        }

        return new JsonCowVariant(modelType, asset);
    }
}