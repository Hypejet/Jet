package net.hypejet.jet.data.json.adapters.model.poi;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.poi.JsonPoiType;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonPoiType point of interest types}.
 *
 * @since 1.0
 * @see JsonPoiType
 * @see TypeAdapterFactory
 */
public final class PoiTypeTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain PoiTypeTypeAdapterFactory point of interest type type adapter factory}.
     *
     * @since 1.0
     */
    public static final PoiTypeTypeAdapterFactory INSTANCE = new PoiTypeTypeAdapterFactory();

    private PoiTypeTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (JsonPoiType.class.isAssignableFrom(type.getRawType())) {
            return new PoiTypeTypeAdapter(gson);
        } else {
            return null;
        }
    }
}