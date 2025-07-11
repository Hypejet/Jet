package net.hypejet.jet.data.json.adapters.model.variant.frog;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.variant.frog.JsonFrogVariant;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonFrogVariant frog variants}.
 *
 * @since 1.0
 * @see JsonFrogVariant
 * @see TypeAdapterFactory
 */
public final class FrogVariantTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain FrogVariantTypeAdapterFactory frog variant type adapter factory}.
     *
     * @since 1.0
     */
    public static final FrogVariantTypeAdapterFactory INSTANCE = new FrogVariantTypeAdapterFactory();

    private FrogVariantTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isAssignableFrom(JsonFrogVariant.class)) {
            return new FrogVariantTypeAdapter(gson);
        } else {
            return null;
        }
    }
}