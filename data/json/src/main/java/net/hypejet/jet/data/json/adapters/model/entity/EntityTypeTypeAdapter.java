package net.hypejet.jet.data.json.adapters.model.entity;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonEntityType entity types}.
 *
 * @since 1.0
 * @see JsonEntityType
 * @see TypeAdapter
 */
final class EntityTypeTypeAdapter extends TypeAdapter<JsonEntityType> {

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
    public void write(JsonWriter out, JsonEntityType value) {
        this.gson.toJson(value.requiredFeatureFlags(), DataJsonTypes.KEY_SET, out);
    }

    @Override
    public JsonEntityType read(JsonReader in) {
        return new JsonEntityType(this.gson.fromJson(in, DataJsonTypes.KEY_SET));
    }
}