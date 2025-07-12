package net.hypejet.jet.data.json.adapters.model.type.dimension;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.type.dimension.JsonDimensionType;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonDimensionType dimension types}.
 *
 * @since 1.0
 * @see JsonDimensionType
 * @see TypeAdapterFactory
 */
public final class DimensionTypeTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain DimensionTypeTypeAdapterFactory dimension type type adapter factory}.
     *
     * @since 1.0
     */
    public static final DimensionTypeTypeAdapterFactory INSTANCE = new DimensionTypeTypeAdapterFactory();

    private DimensionTypeTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (rawType.isAssignableFrom(JsonDimensionType.class)) {
            return new DimensionTypeTypeAdapter(gson);
        } else if (rawType.isAssignableFrom(JsonDimensionType.MonsterSettings.class)) {
            return new DimensionTypeMonsterSettingsTypeAdapter(gson);
        } else {
            return null;
        }
    }
}