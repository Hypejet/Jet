package net.hypejet.jet.data.json.adapters.entry;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.entry.JsonRegistryEntry;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Represents {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonRegistryEntry registry entries}.
 *
 * @since 1.0
 * @see JsonRegistryEntry
 * @see TypeAdapterFactory
 */
public final class RegistryEntryTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain RegistryEntryTypeAdapterFactory registry entry type adapter factory}.
     *
     * @since 1.0
     */
    public static final RegistryEntryTypeAdapterFactory INSTANCE = new RegistryEntryTypeAdapterFactory();

    private RegistryEntryTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (JsonRegistryEntry.FeaturePack.class.isAssignableFrom(rawType)) {
            return FeaturePackTypeAdapter.INSTANCE;
        } else if (JsonRegistryEntry.class.isAssignableFrom(rawType)) {
            if (!(type.getType() instanceof ParameterizedType parameterizedType))
                throw new IllegalArgumentException("The type of registry entry must be a parameterized type");

            Type[] arguments = parameterizedType.getActualTypeArguments();
            if (arguments.length != 1) {
                throw new IllegalArgumentException(
                        "The registry entry parameterized type must have exactly one argument"
                );
            }

            if (!(arguments[0] instanceof Class<?> argument))
                throw new IllegalArgumentException("The registry entry parameterized type argument is not a class");
            return new RegistryEntryTypeAdapter<>(gson, argument);
        } else {
            return null;
        }
    }
}