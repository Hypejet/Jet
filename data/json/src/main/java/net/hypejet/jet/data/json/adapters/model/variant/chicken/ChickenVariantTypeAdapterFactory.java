package net.hypejet.jet.data.json.adapters.model.variant.chicken;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.variant.chicken.JsonChickenVariant;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonChickenVariant chicken variants}.
 *
 * @since 1.0
 * @see JsonChickenVariant
 * @see TypeAdapterFactory
 */
public final class ChickenVariantTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain ChickenVariantTypeAdapterFactory chicken variant type adapter factory}.
     *
     * @since 1.0
     */
    public static final ChickenVariantTypeAdapterFactory INSTANCE = new ChickenVariantTypeAdapterFactory();

    private ChickenVariantTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (rawType.isAssignableFrom(JsonChickenVariant.class)) {
            return new ChickenVariantTypeAdapter(gson);
        } else if (rawType.isAssignableFrom(JsonChickenVariant.ModelType.class)) {
            return ChickenVariantModelTypeTypeAdapter.INSTANCE;
        } else {
            return null;
        }
    }
}