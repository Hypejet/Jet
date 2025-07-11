package net.hypejet.jet.data.json.adapters.model.variant.cow;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.variant.cow.JsonCowVariant;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonCowVariant cow variants}.
 *
 * @since 1.0
 * @see JsonCowVariant
 * @see TypeAdapterFactory
 */
public final class CowVariantTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain CowVariantTypeAdapterFactory cow variant type adapter factory}.
     *
     * @since 1.0
     */
    public static final CowVariantTypeAdapterFactory INSTANCE = new CowVariantTypeAdapterFactory();

    private CowVariantTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (rawType.isAssignableFrom(JsonCowVariant.class)) {
            return new CowVariantTypeAdapter(gson);
        } else if (rawType.isAssignableFrom(JsonCowVariant.ModelType.class)) {
            return CowVariantModelTypeTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}