package net.hypejet.jet.data.json.adapters.model.item;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.item.JsonItem;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonItem items}.
 *
 * @since 1.0
 * @see JsonItem
 * @see TypeAdapter
 */
final class ItemTypeAdapter extends TypeAdapter<JsonItem> {

    private final Gson gson;

    /**
     * Constructs the {@linkplain ItemTypeAdapter item type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    ItemTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonItem value) throws IOException {
        Set<Key> requiredFeatureFlags = value.requiredFeatureFlags();
        if (requiredFeatureFlags.isEmpty()) {
            out.nullValue();
        } else {
            this.gson.toJson(value.requiredFeatureFlags(), DataJsonTypes.KEY_SET, out);
        }
    }

    @Override
    public JsonItem read(JsonReader in) throws IOException {
        return new JsonItem(in.peek() == JsonToken.NULL ? Set.of() : this.gson.fromJson(in, DataJsonTypes.KEY_SET));
    }
}