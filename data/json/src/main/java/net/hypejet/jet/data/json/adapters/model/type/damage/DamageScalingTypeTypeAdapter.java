package net.hypejet.jet.data.json.adapters.model.type.damage;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageScalingType;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonDamageScalingType damage scaling types}.
 *
 * @since 1.0
 * @see JsonDamageScalingType
 * @see TypeAdapter
 */
final class DamageScalingTypeTypeAdapter extends TypeAdapter<JsonDamageScalingType> {
    /**
     * An instance of the {@linkplain DamageScalingTypeTypeAdapter damage scaling type type adapter}.
     *
     * @since 1.0
     */
    static final DamageScalingTypeTypeAdapter INSTANCE = new DamageScalingTypeTypeAdapter();

    private DamageScalingTypeTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonDamageScalingType value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonDamageScalingType read(JsonReader in) throws IOException {
        return JsonDamageScalingType.values()[in.nextInt()];
    }
}