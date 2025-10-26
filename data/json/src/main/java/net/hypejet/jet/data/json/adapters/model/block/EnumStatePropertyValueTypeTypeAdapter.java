package net.hypejet.jet.data.json.adapters.model.block;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.block.state.property.JsonEnumStatePropertyValueType;

import java.io.IOException;

/**
 * A {@linkplain TypeAdapter type adapter} converting
 * {@linkplain JsonEnumStatePropertyValueType enum state property value types}.
 *
 * @since 1.0
 * @see JsonEnumStatePropertyValueType
 * @see TypeAdapter
 */
final class EnumStatePropertyValueTypeTypeAdapter extends TypeAdapter<JsonEnumStatePropertyValueType> {
    /**
     * An instance of
     * the {@linkplain EnumStatePropertyValueTypeTypeAdapter enum state property value type type-adapter}.
     *
     * @since 1.0
     */
    static final EnumStatePropertyValueTypeTypeAdapter INSTANCE = new EnumStatePropertyValueTypeTypeAdapter();

    private EnumStatePropertyValueTypeTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonEnumStatePropertyValueType value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonEnumStatePropertyValueType read(JsonReader in) throws IOException {
        return JsonEnumStatePropertyValueType.values()[in.nextInt()];
    }
}