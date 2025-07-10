package net.hypejet.jet.data.json.adapters.model.trim.material;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.trim.material.JsonMaterialAssetGroup;
import net.hypejet.jet.data.json.model.trim.material.JsonTrimMaterial;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonTrimMaterial trim materials}.
 *
 * @since 1.0
 * @see JsonTrimMaterial
 * @see TypeAdapterFactory
 */
public final class TrimMaterialTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain TrimMaterialTypeAdapterFactory trim material type adapter factory}.
     *
     * @since 1.0
     */
    public static final TrimMaterialTypeAdapterFactory INSTANCE = new TrimMaterialTypeAdapterFactory();

    private TrimMaterialTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (rawType.isAssignableFrom(JsonTrimMaterial.class)) {
            return new TrimMaterialTypeAdapter(gson);
        } else if (rawType.isAssignableFrom(JsonMaterialAssetGroup.class)) {
            return new MaterialAssetGroupTypeAdapter(gson);
        } else if (rawType.isAssignableFrom(JsonMaterialAssetGroup.Asset.class)) {
            return MaterialAssetGroupAssetTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}