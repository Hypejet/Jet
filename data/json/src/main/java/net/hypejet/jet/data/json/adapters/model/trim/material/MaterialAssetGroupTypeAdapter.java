package net.hypejet.jet.data.json.adapters.model.trim.material;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.trim.material.JsonMaterialAssetGroup;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonMaterialAssetGroup material asset groups}.
 *
 * @since 1.0
 * @see JsonMaterialAssetGroup
 * @see TypeAdapter
 */
final class MaterialAssetGroupTypeAdapter extends TypeAdapter<JsonMaterialAssetGroup> {

    private static final String BASE_ASSET_FIELD = "base-asset";
    private static final String OVERRIDES_FIELD = "overrides";

    private final Gson gson;

    /**
     * Constructs the {@linkplain MaterialAssetGroupTypeAdapter material asset group type adapter}.
     *
     * @param gson a gson objects to convert other objects with
     * @since 1.0
     */
    MaterialAssetGroupTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonMaterialAssetGroup value) throws IOException {
        out.beginObject();

        out.name(BASE_ASSET_FIELD);
        this.gson.toJson(value.baseAsset(), JsonMaterialAssetGroup.Asset.class, out);

        Map<Key, JsonMaterialAssetGroup.Asset> overrides = value.overrides();
        if (!overrides.isEmpty()) {
            out.name(OVERRIDES_FIELD);
            this.writeOverrides(out, value.overrides());
        }

        out.endObject();
    }

    @Override
    public JsonMaterialAssetGroup read(JsonReader in) throws IOException {
        in.beginObject();

        JsonMaterialAssetGroup.Asset baseAsset = null;
        Map<Key, JsonMaterialAssetGroup.Asset> overrides = new HashMap<>();

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case BASE_ASSET_FIELD -> baseAsset = this.gson.fromJson(in, JsonMaterialAssetGroup.Asset.class);
                case OVERRIDES_FIELD -> this.readOverrides(in, overrides);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (baseAsset == null)
            throw new JsonParseException("The base asset has not been specified");
        return new JsonMaterialAssetGroup(baseAsset, overrides);
    }

    private void writeOverrides(@NonNull JsonWriter out,
                                @NonNull Map<Key, JsonMaterialAssetGroup.Asset> overrides) throws IOException {
        out.beginObject();

        for (Map.Entry<Key, JsonMaterialAssetGroup.Asset> entry : overrides.entrySet()) {
            out.name(entry.getKey().asString());
            this.gson.toJson(entry.getValue(), JsonMaterialAssetGroup.Asset.class, out);
        }

        out.endObject();
    }

    private void readOverrides(@NonNull JsonReader in,
                               @NonNull Map<Key, JsonMaterialAssetGroup.Asset> overrides) throws IOException {
        in.beginObject();

        while (in.peek() == JsonToken.NAME) {
            Key key = Key.key(in.nextName());
            JsonMaterialAssetGroup.Asset asset = this.gson.fromJson(in, JsonMaterialAssetGroup.Asset.class);
            overrides.put(key, asset);
        }

        in.endObject();
    }
}