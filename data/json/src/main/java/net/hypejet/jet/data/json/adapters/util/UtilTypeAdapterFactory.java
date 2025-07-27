package net.hypejet.jet.data.json.adapters.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.util.JsonUnit;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * of utility objects.
 *
 * @since 1.0
 * @see TypeAdapterFactory
 */
public final class UtilTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain UtilTypeAdapterFactory util type adapter factory}.
     *
     * @since 1.0
     */
    public static final UtilTypeAdapterFactory INSTANCE = new UtilTypeAdapterFactory();

    private UtilTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (JsonUnit.class.isAssignableFrom(type.getRawType())) {
            return UnitTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}