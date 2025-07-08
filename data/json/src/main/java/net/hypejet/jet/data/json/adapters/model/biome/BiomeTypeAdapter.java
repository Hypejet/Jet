package net.hypejet.jet.data.json.adapters.model.biome;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.biome.JsonBiome;
import net.hypejet.jet.data.json.model.biome.JsonBiomeSpecialEffects;
import net.hypejet.jet.data.json.model.biome.JsonClimateSettings;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonBiome biomes}.
 *
 * @since 1.0
 * @see JsonBiome
 * @see TypeAdapter
 */
final class BiomeTypeAdapter extends TypeAdapter<JsonBiome> {

    private static final String CLIMATE_SETTINGS_FIELD = "climate-settings";
    private static final String SPECIAL_EFFECTS_FIELD = "special-effects";

    private final Gson gson;

    /**
     * Constructs the {@linkplain BiomeTypeAdapter biome type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    BiomeTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonBiome value) throws IOException {
        out.beginObject();

        out.name(CLIMATE_SETTINGS_FIELD);
        this.gson.toJson(value.climateSettings(), JsonClimateSettings.class, out);

        out.name(SPECIAL_EFFECTS_FIELD);
        this.gson.toJson(value.biomeSpecialEffects(), JsonBiomeSpecialEffects.class, out);

        out.endObject();
    }

    @Override
    public JsonBiome read(JsonReader in) throws IOException {
        in.beginObject();

        JsonClimateSettings climateSettings = null;
        JsonBiomeSpecialEffects specialEffects = null;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case CLIMATE_SETTINGS_FIELD -> climateSettings = this.gson.fromJson(in, JsonClimateSettings.class);
                case SPECIAL_EFFECTS_FIELD -> specialEffects = this.gson.fromJson(in, JsonBiomeSpecialEffects.class);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (climateSettings == null) {
            throw new JsonParseException("The climate settings field has not been specified");
        } else if (specialEffects == null) {
            throw new JsonParseException("The special effects field has not been specified");
        }

        return new JsonBiome(climateSettings, specialEffects);
    }
}