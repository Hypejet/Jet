package net.hypejet.jet.data.json.adapters.guava;

import com.google.common.primitives.ImmutableIntArray;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * of guava objects.
 *
 * @since 1.0
 * @see TypeAdapterFactory
 */
public final class GuavaTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain GuavaTypeAdapterFactory guava type adapter factory}.
     *
     * @since 1.0
     */
    public static final GuavaTypeAdapterFactory INSTANCE = new GuavaTypeAdapterFactory();

    private GuavaTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (ImmutableIntArray.class.isAssignableFrom(type.getRawType())) {
            return ImmutableIntArrayTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}