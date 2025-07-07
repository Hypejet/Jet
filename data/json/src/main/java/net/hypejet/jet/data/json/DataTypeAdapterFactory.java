package net.hypejet.jet.data.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.adapters.DataEntryTypeAdapter;
import net.hypejet.jet.data.json.adapters.KeyAdapter;
import net.hypejet.jet.data.json.entry.DataEntry;
import net.kyori.adventure.key.Key;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
        if (Key.class.isAssignableFrom(rawType)) {
            return KeyAdapter.INSTANCE;
        } else if (DataEntry.class.isAssignableFrom(type.getRawType())) {
            if (!(type.getType() instanceof ParameterizedType parameterizedType))
                throw new IllegalArgumentException("The data entry type must be a parameterized type");

            Type[] arguments = parameterizedType.getActualTypeArguments();
            if (arguments.length != 1)
                throw new IllegalArgumentException("The data entry parameterized type must have exactly one argument");

            if (!(arguments[0] instanceof Class<?> argument))
                throw new IllegalArgumentException("The data entry parameterized type argument is not a class");
            return new DataEntryTypeAdapter<>(gson, argument);
        } else {
            return null;
        }
    }
}