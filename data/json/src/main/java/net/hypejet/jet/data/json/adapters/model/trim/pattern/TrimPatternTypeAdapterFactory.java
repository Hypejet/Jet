package net.hypejet.jet.data.json.adapters.model.trim.pattern;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.trim.pattern.JsonTrimPattern;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonTrimPattern trim patterns}.
 *
 * @since 1.0
 * @see JsonTrimPattern
 * @see TypeAdapterFactory
 */
public final class TrimPatternTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain TrimPatternTypeAdapterFactory trim pattern type adapter factory}.
     *
     * @since 1.0
     */
    public static final TrimPatternTypeAdapterFactory INSTANCE = new TrimPatternTypeAdapterFactory();

    private TrimPatternTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isAssignableFrom(JsonTrimPattern.class)) {
            return new TrimPatternTypeAdapter(gson);
        } else {
            return null;
        }
    }
}