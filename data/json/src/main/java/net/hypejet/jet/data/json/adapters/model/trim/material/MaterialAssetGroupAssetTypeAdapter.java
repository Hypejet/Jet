package net.hypejet.jet.data.json.adapters.model.trim.material;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.trim.material.JsonMaterialAssetGroup;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter}
 * of {@linkplain JsonMaterialAssetGroup.Asset material asset group assets}.
 *
 * @since 1.0
 * @see JsonMaterialAssetGroup.Asset
 * @see TypeAdapter
 */
final class MaterialAssetGroupAssetTypeAdapter extends TypeAdapter<JsonMaterialAssetGroup.Asset> {
    /**
     * An instance of the {@linkplain MaterialAssetGroupAssetTypeAdapter material asset group asset type adapter}.
     *
     * @since 1.0
     */
    static final MaterialAssetGroupAssetTypeAdapter INSTANCE = new MaterialAssetGroupAssetTypeAdapter();

    private MaterialAssetGroupAssetTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonMaterialAssetGroup.Asset value) throws IOException {
        out.value(value.value());
    }

    @Override
    public JsonMaterialAssetGroup.Asset read(JsonReader in) throws IOException {
        return new JsonMaterialAssetGroup.Asset(in.nextString());
    }
}