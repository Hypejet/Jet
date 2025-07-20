package net.hypejet.jet.data.json.adapters.model.enchantment;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.model.enchantment.JsonEnchantment;

/**
 * Represents a {@linkplain TypeAdapterFactory type adapter factory} providing {@linkplain TypeAdapter type adapters}
 * of objects related to {@linkplain JsonEnchantment enchantments}.
 *
 * @since 1.0
 * @see JsonEnchantment
 * @see TypeAdapterFactory
 */
public final class EnchantmentTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * An instance of the {@linkplain EnchantmentTypeAdapterFactory enchantment type adapter factory}.
     *
     * @since 1.0
     */
    public static final EnchantmentTypeAdapterFactory INSTANCE = new EnchantmentTypeAdapterFactory();

    private EnchantmentTypeAdapterFactory() {}

    @Override
    public <T> TypeAdapter create(Gson gson, TypeToken<T> type) {
        if (JsonEnchantment.class.isAssignableFrom(type.getRawType())) {
            return new EnchantmentTypeAdapter(gson);
        } else {
            return null;
        }
    }
}