package net.hypejet.jet.data.json.adapters.model.biome;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.biome.JsonAmbientParticleSettings;

/**
 * Represents a {@linkplain TypeAdapter type adapter}
 * of {@linkplain JsonAmbientParticleSettings ambient particle settings}.
 *
 * @since 1.0
 * @see JsonAmbientParticleSettings
 * @see TypeAdapter
 */
final class AmbientParticleSettingsTypeAdapter extends TypeAdapter<JsonAmbientParticleSettings> {
    @Override
    public void write(JsonWriter out, JsonAmbientParticleSettings value) {
        // TODO
    }

    @Override
    public JsonAmbientParticleSettings read(JsonReader in) {
        return new JsonAmbientParticleSettings(); // TODO
    }
}