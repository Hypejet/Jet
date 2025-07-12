package net.hypejet.jet.data.json.adapters.model.type.damage;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageEffects;

import java.io.IOException;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonDamageEffects damage effects}.
 *
 * @since 1.0
 * @see JsonDamageEffects
 * @see TypeAdapter
 */
final class DamageEffectsTypeAdapter extends TypeAdapter<JsonDamageEffects> {
    /**
     * An instance of the {@linkplain DamageEffectsTypeAdapter damage effects type adapter}.
     *
     * @since 1.0
     */
    static final DamageEffectsTypeAdapter INSTANCE = new DamageEffectsTypeAdapter();

    private DamageEffectsTypeAdapter() {}

    @Override
    public void write(JsonWriter out, JsonDamageEffects value) throws IOException {
        out.value(value.ordinal());
    }

    @Override
    public JsonDamageEffects read(JsonReader in) throws IOException {
        return JsonDamageEffects.values()[in.nextInt()];
    }
}