package net.hypejet.jet.data.json.adapters.model.entity;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonEntityType entity types}.
 *
 * @since 1.0
 * @see JsonEntityType
 * @see TypeAdapter
 */
final class EntityTypeTypeAdapter extends TypeAdapter<JsonEntityType> {

    private static final String REQUIRED_FEATURE_FLAGS_FIELD = "required_features";
    private static final String MAX_AIR_SUPPLY_FIELD = "max_air_supply";

    private static final int DEFAULT_MAX_AIR_SUPPLY = 300;

    private final Gson gson;

    /**
     * Constructs the {@linkplain EntityTypeTypeAdapter entity type type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    EntityTypeTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonEntityType value) throws IOException {
        out.beginObject();

        out.name(REQUIRED_FEATURE_FLAGS_FIELD);
        this.gson.toJson(value.requiredFeatureFlags(), DataJsonTypes.KEY_SET, out);

        int maxAirSupply = value.maxAirSupply();
        if (maxAirSupply != DEFAULT_MAX_AIR_SUPPLY) {
            out.name(MAX_AIR_SUPPLY_FIELD);
            out.value(value.maxAirSupply());
        }

        out.endObject();
    }

    @Override
    public JsonEntityType read(JsonReader in) throws IOException {
        in.beginObject();

        Set<Key> requiredFeatureFlags = null;
        int maxAirSupply = DEFAULT_MAX_AIR_SUPPLY;

        while (in.peek() == JsonToken.NAME) {
            switch (in.nextName()) {
                case REQUIRED_FEATURE_FLAGS_FIELD ->
                        requiredFeatureFlags = this.gson.fromJson(in, DataJsonTypes.KEY_SET);
                case MAX_AIR_SUPPLY_FIELD -> maxAirSupply = in.nextInt();
            }
        }

        in.endObject();

        if (requiredFeatureFlags == null) {
            throw new IllegalArgumentException("The required feature flags have not been specified");
        } else {
            return new JsonEntityType(requiredFeatureFlags, maxAirSupply);
        }
    }
}