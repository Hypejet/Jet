package net.hypejet.jet.data.json.adapters.model.pattern.banner;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.pattern.banner.JsonBannerPattern;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonBannerPattern banner patterns}.
 *
 * @since 1.0
 * @see JsonBannerPattern
 * @see TypeAdapterFactory
 */
public final class BannerPatternTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain BannerPatternTypeAdapterFactory banner pattern type adapter factory}.
     *
     * @since 1.0
     */
    public static final BannerPatternTypeAdapterFactory INSTANCE = new BannerPatternTypeAdapterFactory();

    private BannerPatternTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isAssignableFrom(JsonBannerPattern.class)) {
            return new BannerPatternTypeAdapter(gson);
        } else {
            return null;
        }
    }
}