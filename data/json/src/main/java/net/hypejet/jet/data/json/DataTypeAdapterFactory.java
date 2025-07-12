package net.hypejet.jet.data.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.adapters.DataEntryTypeAdapter;
import net.hypejet.jet.data.json.adapters.FeaturePackTypeAdapter;
import net.hypejet.jet.data.json.adapters.IntProviderTypeAdapter;
import net.hypejet.jet.data.json.adapters.KeyTypeAdapter;
import net.hypejet.jet.data.json.adapters.model.HolderTypeAdapter;
import net.hypejet.jet.data.json.adapters.model.SoundEventTypeAdapter;
import net.hypejet.jet.data.json.adapters.model.WeightedTypeAdapter;
import net.hypejet.jet.data.json.entry.DataEntry;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonIntProvider;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.model.JsonWeighted;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

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
        Type actualType = type.getType();

        if (Key.class.isAssignableFrom(rawType)) {
            return KeyTypeAdapter.INSTANCE;
        } else if (DataEntry.FeaturePack.class.isAssignableFrom(rawType)) {
            return FeaturePackTypeAdapter.INSTANCE;
        } else if (JsonSoundEvent.class.isAssignableFrom(rawType)) {
            return new SoundEventTypeAdapter(gson);
        } else if (JsonIntProvider.class.isAssignableFrom(rawType)) {
            return new IntProviderTypeAdapter(gson);
        } else if (JsonHolder.class.isAssignableFrom(rawType)) {
            return new HolderTypeAdapter<>(gson, onlyParameterizedArgument(actualType));
        } else if (JsonWeighted.class.isAssignableFrom(rawType)) {
            return new WeightedTypeAdapter<>(gson, onlyParameterizedArgument(actualType));
        } else if (DataEntry.class.isAssignableFrom(rawType)) {
            return new DataEntryTypeAdapter<>(gson, onlyParameterizedArgument(actualType));
        } else {
            return null;
        }
    }

    private static @NonNull Class<?> onlyParameterizedArgument(@NonNull Type type) {
        if (!(type instanceof ParameterizedType parameterizedType))
            throw new IllegalArgumentException("The specified type must be a parameterized type");

        Type[] arguments = parameterizedType.getActualTypeArguments();
        if (arguments.length != 1)
            throw new IllegalArgumentException("The specified parameterized type must have exactly one argument");

        if (!(arguments[0] instanceof Class<?> argument))
            throw new IllegalArgumentException("The specified parameterized type argument is not a class");
        return argument;
    }
}