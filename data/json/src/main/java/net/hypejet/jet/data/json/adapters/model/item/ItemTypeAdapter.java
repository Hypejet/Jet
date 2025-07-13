package net.hypejet.jet.data.json.adapters.model.item;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.item.JsonItem;
import net.hypejet.jet.data.json.util.JsonUtil;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.HashSet;
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
        JsonUtil.writeCollection(out, this.gson, Key.class, value.requiredFeatureFlags());
    }

    @Override
    public JsonItem read(JsonReader in) throws IOException {
        Set<Key> requiredFeatureFlags = new HashSet<>();
        JsonUtil.readCollection(in, this.gson, Key.class, requiredFeatureFlags);
        return new JsonItem(requiredFeatureFlags);
    }
}