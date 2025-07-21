package net.hypejet.jet.data.json.adapters.model.block;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.JsonBlockEntityType;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * of objects related to {@linkplain JsonBlock blocks}.
 *
 * @since 1.0
 * @see JsonBlock
 * @see TypeAdapterFactory
 */
public final class BlockTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain BlockTypeAdapterFactory block type adapter factory}.
     *
     * @since 1.0
     */
    public static final BlockTypeAdapterFactory INSTANCE = new BlockTypeAdapterFactory();

    private BlockTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (JsonBlock.class.isAssignableFrom(rawType)) {
            return new BlockTypeAdapter(gson);
        } else if (JsonBlockEntityType.class.isAssignableFrom(rawType)) {
            return new BlockEntityTypeTypeAdapter(gson);
        } else {
            return null;
        }
    }
}