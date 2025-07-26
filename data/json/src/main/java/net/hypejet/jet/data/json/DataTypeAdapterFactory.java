package net.hypejet.jet.data.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.adapters.UnitTypeAdapter;
import net.hypejet.jet.data.json.util.JsonUnit;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting Jet data objects.
 *
 * @since 1.0
 * @see TypeAdapterFactory
 */
public final class DataTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain DataTypeAdapterFactory data type adapter factory}.
     *
     * @since 1.0
     */
    public static final DataTypeAdapterFactory INSTANCE = new DataTypeAdapterFactory();

    private DataTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (JsonUnit.class.isAssignableFrom(rawType)) {
            return UnitTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}