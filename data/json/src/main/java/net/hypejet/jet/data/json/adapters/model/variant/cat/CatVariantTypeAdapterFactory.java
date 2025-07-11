package net.hypejet.jet.data.json.adapters.model.variant.cat;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.variant.cat.JsonCatVariant;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonCatVariant cat variants}.
 *
 * @since 1.0
 * @see JsonCatVariant
 * @see TypeAdapterFactory
 */
public final class CatVariantTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain CatVariantTypeAdapterFactory cat variant type adapter factory}.
     *
     * @since 1.0
     */
    public static final CatVariantTypeAdapterFactory INSTANCE = new CatVariantTypeAdapterFactory();

    private CatVariantTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isAssignableFrom(JsonCatVariant.class)) {
            return new CatVariantTypeAdapter(gson);
        } else {
            return null;
        }
    }
}