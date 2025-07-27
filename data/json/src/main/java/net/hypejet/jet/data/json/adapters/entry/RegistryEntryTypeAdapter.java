package net.hypejet.jet.data.json.adapters.entry;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonRegistryEntry registry entries}.
 *
 * @param <V> a type of value of the registry entries that the type adapter converts
 * @since 1.0
 * @see JsonRegistryEntry
 * @see TypeAdapter
 */
final class RegistryEntryTypeAdapter<V> extends TypeAdapter<JsonRegistryEntry<V>> {

    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";
    private static final String TAGS_FIELD = "tags";
    private static final String KNOWN_PACK_FIELD = "known-pack";

    private final Gson gson;
    private final Class<V> valueClass;

    /**
     * Constructs the {@linkplain RegistryEntryTypeAdapter registry entry type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @param valueClass a class of values of the registry entries that the type adapter should convert
     * @since 1.0
     */
    RegistryEntryTypeAdapter(@NonNull Gson gson, @NonNull Class<V> valueClass) {
        this.gson = Objects.requireNonNull(gson, "gson");
        this.valueClass = Objects.requireNonNull(valueClass, "value class");
    }

    @Override
    public void write(JsonWriter out, JsonRegistryEntry<V> value) throws IOException {
        out.beginObject();

        out.name(KEY_FIELD);
        this.gson.toJson(value.key(), Key.class, out);

        out.name(VALUE_FIELD);
        this.gson.toJson(value.value(), this.valueClass, out);

        Set<Key> tags = value.tags();
        if (!tags.isEmpty()) {
            out.name(TAGS_FIELD);
            this.gson.toJson(value.tags(), DataJsonTypes.KEY_SET, out);
        }

        JsonRegistryEntry.FeaturePack knownPack = value.knownPack();
        if (knownPack != null) {
            out.name(KNOWN_PACK_FIELD);
            this.gson.toJson(knownPack, JsonRegistryEntry.FeaturePack.class, out);
        }

        out.endObject();
    }

    @Override
    public JsonRegistryEntry<V> read(JsonReader in) throws IOException {
        Key key = null;
        V value = null;
        Set<Key> tags = Set.of();
        JsonRegistryEntry.FeaturePack knownPack = null;

        in.beginObject();
        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case KEY_FIELD -> key = this.gson.fromJson(in, Key.class);
                case VALUE_FIELD -> value = this.gson.fromJson(in, this.valueClass);
                case TAGS_FIELD -> tags = this.gson.fromJson(in, DataJsonTypes.KEY_SET);
                case KNOWN_PACK_FIELD -> knownPack = this.gson.fromJson(in, JsonRegistryEntry.FeaturePack.class);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }
        in.endObject();

        if (key == null) {
            throw new JsonParseException("The key field has not been specified");
        } else if (value == null) {
            throw new JsonParseException("The value field has not been specified");
        }

        return new JsonRegistryEntry<>(key, value, tags, knownPack);
    }
}