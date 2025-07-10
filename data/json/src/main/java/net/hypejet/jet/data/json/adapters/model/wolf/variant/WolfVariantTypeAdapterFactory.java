package net.hypejet.jet.data.json.adapters.model.wolf.variant;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.variant.wolf.JsonWolfVariant;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonWolfVariant wolf variants}.
 *
 * @since 1.0
 * @see JsonWolfVariant
 * @see TypeAdapterFactory
 */
public final class WolfVariantTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain WolfVariantTypeAdapterFactory wolf variant type adapter factory}.
     *
     * @since 1.0
     */
    public static final WolfVariantTypeAdapterFactory INSTANCE = new WolfVariantTypeAdapterFactory();

    private WolfVariantTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isAssignableFrom(JsonWolfVariant.class)) {
            return new WolfVariantTypeAdapter(gson);
        } else {
            return null;
        }
    }
}