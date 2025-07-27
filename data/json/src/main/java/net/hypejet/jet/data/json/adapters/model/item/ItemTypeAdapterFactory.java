package net.hypejet.jet.data.json.adapters.model.item;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.item.JsonItem;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonItem items}.
 *
 * @since 1.0
 * @see JsonItem
 * @see TypeAdapterFactory
 */
public final class ItemTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain ItemTypeAdapterFactory item type adapter factory}.
     *
     * @since 1.0
     */
    public static final ItemTypeAdapterFactory INSTANCE = new ItemTypeAdapterFactory();

    private ItemTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (JsonItem.class.isAssignableFrom(type.getRawType())) {
            return new ItemTypeAdapter(gson);
        } else {
            return null;
        }
    }
}