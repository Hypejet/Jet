package net.hypejet.jet.data.json.adapters.adventure;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * of adventure types.
 *
 * @since 1.0
 * @see TypeAdapterFactory
 */
public final class AdventureTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain AdventureTypeAdapterFactory adventure type adapter factory}.
     *
     * @since 1.0
     */
    public static final AdventureTypeAdapterFactory INSTANCE = new AdventureTypeAdapterFactory();

    private AdventureTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (BinaryTagHolder.class.isAssignableFrom(rawType)) {
            return BinaryTagHolderTypeAdapter.INSTANCE;
        } else if (Key.class.isAssignableFrom(rawType)) {
            return KeyTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}