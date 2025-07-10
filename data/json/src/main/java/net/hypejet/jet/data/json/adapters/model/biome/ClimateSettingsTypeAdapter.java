package net.hypejet.jet.data.json.adapters.model.biome;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.biome.JsonClimateSettings;
import net.hypejet.jet.data.json.model.biome.JsonTemperatureModifier;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonClimateSettings climate settings}.
 *
 * @since 1.0
 * @see JsonClimateSettings
 * @see TypeAdapter
 */
final class ClimateSettingsTypeAdapter extends TypeAdapter<JsonClimateSettings> {

    private static final String HAS_PRECIPITATION_FIELD = "has-precipitation";
    private static final String TEMPERATURE_FIELD = "temperature";
    private static final String TEMPERATURE_MODIFIER_FIELD = "temperature-modifier";
    private static final String DOWNFALL_FIELD = "downfall";

    private final Gson gson;

    /**
     * Constructs the {@linkplain ClimateSettingsTypeAdapter climate settings type adapter}.
     *
     * @param gson a gson object to convert other objects wtih
     * @since 1.0
     */
    ClimateSettingsTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonClimateSettings value) throws IOException {
        out.beginObject();

        out.name(HAS_PRECIPITATION_FIELD);
        out.value(value.hasPrecipitation());

        out.name(TEMPERATURE_FIELD);
        out.value(value.temperature());

        out.name(TEMPERATURE_MODIFIER_FIELD);
        this.gson.toJson(value.temperatureModifier(), JsonTemperatureModifier.class, out);

        out.name(DOWNFALL_FIELD);
        out.value(value.downfall());

        out.endObject();
    }

    @Override
    public JsonClimateSettings read(JsonReader in) throws IOException {
        in.beginObject();

        boolean hasPrecipitation = false;
        float temperature = 0;
        JsonTemperatureModifier temperatureModifier = null;
        float downfall = 0;

        boolean hasPrecipitationInitialized = false;
        boolean temperatureInitialized = false;
        boolean downfallInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case HAS_PRECIPITATION_FIELD -> {
                    hasPrecipitation = in.nextBoolean();
                    hasPrecipitationInitialized = true;
                }
                case TEMPERATURE_FIELD -> {
                    temperature = (float) in.nextDouble();
                    temperatureInitialized = true;
                }
                case TEMPERATURE_MODIFIER_FIELD ->
                        temperatureModifier = this.gson.fromJson(in, JsonTemperatureModifier.class);
                case DOWNFALL_FIELD -> {
                    downfall = (float) in.nextDouble();
                    downfallInitialized = true;
                }
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (!hasPrecipitationInitialized) {
            throw new JsonParseException("The has precipitation field has not been specified");
        } else if (!temperatureInitialized) {
            throw new JsonParseException("The temperature field has not been specified");
        } else if (temperatureModifier == null) {
            throw new JsonParseException("The temperature modifier field has not been specified");
        } else if (!downfallInitialized) {
            throw new JsonParseException("The downfall field has not been specified");
        }

        return new JsonClimateSettings(hasPrecipitation, temperature, temperatureModifier, downfall);
    }
}