package net.hypejet.jet.data.json.adapters.model.entity;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.entity.JsonEntityType;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonEntityType entity types}.
 *
 * @since 1.0
 * @see JsonEntityType
 * @see TypeAdapterFactory
 */
public final class EntityTypeTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain EntityTypeTypeAdapterFactory entity type type adapter factory}.
     *
     * @since 1.0
     */
    public static final EntityTypeTypeAdapterFactory INSTANCE = new EntityTypeTypeAdapterFactory();

    private EntityTypeTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (JsonEntityType.class.isAssignableFrom(type.getRawType())) {
            return new EntityTypeTypeAdapter(gson);
        } else {
            return null;
        }
    }
}