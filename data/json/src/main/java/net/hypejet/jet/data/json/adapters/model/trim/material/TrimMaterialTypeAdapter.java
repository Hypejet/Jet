package net.hypejet.jet.data.json.adapters.model.trim.material;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.trim.material.JsonMaterialAssetGroup;
import net.hypejet.jet.data.json.model.trim.material.JsonTrimMaterial;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonTrimMaterial trim materials}.
 *
 * @since 1.0
 * @see JsonTrimMaterial
 * @see TypeAdapter
 */
final class TrimMaterialTypeAdapter extends TypeAdapter<JsonTrimMaterial> {

    private static final String ASSETS_FIELD = "assets";
    private static final String DESCRIPTION_FIELD = "description";

    private final Gson gson;

    /**
     * Constructs the {@linkplain TrimMaterialTypeAdapter trim material type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    TrimMaterialTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonTrimMaterial value) throws IOException {
        out.beginObject();

        out.name(ASSETS_FIELD);
        this.gson.toJson(value.assets(), JsonMaterialAssetGroup.class, out);

        out.name(DESCRIPTION_FIELD);
        this.gson.toJson(value.description(), Component.class, out);

        out.endObject();
    }

    @Override
    public JsonTrimMaterial read(JsonReader in) throws IOException {
        in.beginObject();

        JsonMaterialAssetGroup assets = null;
        Component description = null;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case ASSETS_FIELD -> assets = this.gson.fromJson(in, JsonMaterialAssetGroup.class);
                case DESCRIPTION_FIELD -> description = this.gson.fromJson(in, Component.class);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (assets == null) {
            throw new JsonParseException("The assets have not been specified");
        } else if (description == null) {
            throw new JsonParseException("The description has not been specified");
        }

        return new JsonTrimMaterial(assets, description);
    }
}