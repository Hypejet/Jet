package net.hypejet.jet.data.json.adapters.model.variant.painting;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.variant.painting.JsonPaintingVariant;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonPaintingVariant painting variants}.
 *
 * @since 1.0
 * @see JsonPaintingVariant
 * @see TypeAdapterFactory
 */
public final class PaintingVariantTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain PaintingVariantTypeAdapterFactory painting variant type adapter factory}.
     *
     * @since 1.0
     */
    public static final PaintingVariantTypeAdapterFactory INSTANCE = new PaintingVariantTypeAdapterFactory();

    private PaintingVariantTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isAssignableFrom(JsonPaintingVariant.class)) {
            return new PaintingVariantTypeAdapter(gson);
        } else {
            return null;
        }
    }
}