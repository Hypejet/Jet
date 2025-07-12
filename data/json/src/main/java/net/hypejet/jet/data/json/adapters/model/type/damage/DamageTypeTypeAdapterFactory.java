package net.hypejet.jet.data.json.adapters.model.type.damage;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageEffects;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageScalingType;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageType;
import net.hypejet.jet.data.json.model.type.damage.JsonDeathMessageType;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * converting objects related to {@linkplain JsonDamageType damage types}.
 *
 * @since 1.0
 * @see JsonDamageType
 * @see TypeAdapterFactory
 */
public final class DamageTypeTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain DamageTypeTypeAdapterFactory damage type type adapter factory}.
     *
     * @since 1.0
     */
    public static final DamageTypeTypeAdapterFactory INSTANCE = new DamageTypeTypeAdapterFactory();

    private DamageTypeTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (rawType.isAssignableFrom(JsonDamageEffects.class)) {
            return DamageEffectsTypeAdapter.INSTANCE;
        } else if (rawType.isAssignableFrom(JsonDamageScalingType.class)) {
            return DamageScalingTypeTypeAdapter.INSTANCE;
        } else if (rawType.isAssignableFrom(JsonDeathMessageType.class)) {
            return DeathMessageTypeTypeAdapter.INSTANCE;
        } else if (rawType.isAssignableFrom(JsonDamageType.class)) {
            return new DamageTypeTypeAdapter(gson);
        } else {
            return null;
        }
    }
}