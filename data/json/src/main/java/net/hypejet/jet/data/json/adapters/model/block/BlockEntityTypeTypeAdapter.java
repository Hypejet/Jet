package net.hypejet.jet.data.json.adapters.model.block;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.block.JsonBlockEntityType;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonBlockEntityType block entity types}.
 *
 * @since 1.0
 * @see JsonBlockEntityType
 * @see TypeAdapter
 */
final class BlockEntityTypeTypeAdapter extends TypeAdapter<JsonBlockEntityType> {

    private final Gson gson;

    /**
     * Constructs the {@linkplain BlockEntityTypeTypeAdapter block entity type type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    BlockEntityTypeTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonBlockEntityType value) {
        this.gson.toJson(value.validBlocks(), DataJsonTypes.KEY_SET, out);
    }

    @Override
    public JsonBlockEntityType read(JsonReader in) {
        return new JsonBlockEntityType(this.gson.fromJson(in, DataJsonTypes.KEY_SET));
    }
}