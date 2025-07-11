package net.hypejet.jet.data.json.adapters.model.variant.pig;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.variant.pig.JsonPigVariant;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonPigVariant pig variants}.
 *
 * @since 1.0
 * @see JsonPigVariant
 * @see TypeAdapterFactory
 */
public final class PigVariantTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain PigVariantTypeAdapterFactory pig variant type adapter factory}.
     *
     * @since 1.0
     */
    public static final PigVariantTypeAdapterFactory INSTANCE = new PigVariantTypeAdapterFactory();

    private PigVariantTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (rawType.isAssignableFrom(JsonPigVariant.class)) {
            return new PigVariantTypeAdapter(gson);
        } else if (rawType.isAssignableFrom(JsonPigVariant.ModelType.class)) {
            return PigVariantModelTypeTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}