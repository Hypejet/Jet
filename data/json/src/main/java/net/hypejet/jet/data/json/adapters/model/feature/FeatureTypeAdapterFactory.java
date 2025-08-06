package net.hypejet.jet.data.json.adapters.model.feature;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.feature.JsonKnownPack;

/**
 * A {@linkplain TypeAdapterFactory type adapter factory} providing
 * {@linkplain TypeAdapter type adapters} converting objects related to feature packs.
 *
 * @since 1.0
 * @see TypeAdapterFactory
 */
public final class FeatureTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain FeatureTypeAdapterFactory feature type adapter factory}.
     *
     * @since 1.0
     */
    public static final FeatureTypeAdapterFactory INSTANCE = new FeatureTypeAdapterFactory();

    private FeatureTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (JsonKnownPack.class.isAssignableFrom(type.getRawType())) {
            return KnownPackTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}