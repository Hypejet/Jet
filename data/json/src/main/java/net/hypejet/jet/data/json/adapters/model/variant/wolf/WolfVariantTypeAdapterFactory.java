package net.hypejet.jet.data.json.adapters.model.variant.wolf;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.variant.wolf.JsonWolfSoundVariant;
import net.hypejet.jet.data.json.model.variant.wolf.JsonWolfVariant;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing
 * {@linkplain TypeAdapter type adapters} converting objects related
 * to {@linkplain JsonWolfVariant wolf variants} and {@linkplain JsonWolfSoundVariant wolf sound variants}.
 *
 * @since 1.0
 * @see JsonWolfVariant
 * @see JsonWolfSoundVariant
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
        Class<? super T> rawType = type.getRawType();
        if (rawType.isAssignableFrom(JsonWolfVariant.class)) {
            return new WolfVariantTypeAdapter(gson);
        } else if (rawType.isAssignableFrom(JsonWolfSoundVariant.class)) {
            return new WolfSoundVariantTypeAdapter(gson);
        } else {
            return null;
        }
    }
}