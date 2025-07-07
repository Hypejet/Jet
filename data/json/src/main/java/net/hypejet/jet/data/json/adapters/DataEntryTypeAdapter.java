package net.hypejet.jet.data.json.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.entry.DataEntry;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain DataEntry data entries}.
 *
 * @param <V> a type of value of the data entries that the type adapter converts
 * @since 1.0
 * @see DataEntry
 * @see TypeAdapter
 */
public final class DataEntryTypeAdapter<V> extends TypeAdapter<DataEntry<V>> {

    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";
    private static final String REQUIRED_PACKS_FIELD = "packs";

    private final Gson gson;
    private final Class<V> valueClass;

    /**
     * Constructs the {@linkplain DataEntryTypeAdapter data entry type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @param valueClass a class of values of the data entries that the type adapter should convert
     * @since 1.0
     */
    public DataEntryTypeAdapter(@NonNull Gson gson, @NonNull Class<V> valueClass) {
        this.gson = Objects.requireNonNull(gson, "gson");
        this.valueClass = Objects.requireNonNull(valueClass, "value class");
    }

    @Override
    public void write(JsonWriter out, DataEntry<V> value) throws IOException {
        out.beginObject();

        out.name(KEY_FIELD);
        this.gson.toJson(value.key(), Key.class, out);

        out.name(VALUE_FIELD);
        this.gson.toJson(value.value(), this.valueClass, out);

        Set<Key> requiredPacks = value.requiredPacks();
        if (!requiredPacks.isEmpty()) {
            out.name(REQUIRED_PACKS_FIELD);
            out.beginArray();
            requiredPacks.forEach(key -> this.gson.toJson(key, Key.class, out));
            out.endArray();
        }

        out.endObject();
    }

    @Override
    public DataEntry<V> read(JsonReader in) throws IOException {
        Key key = null;
        V value = null;
        Set<Key> requiredPacks = new HashSet<>();

        in.beginObject();
        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case KEY_FIELD -> key = this.gson.fromJson(in, Key.class);
                case VALUE_FIELD -> value = this.gson.fromJson(in, this.valueClass);
                case REQUIRED_PACKS_FIELD -> {
                    in.beginArray();
                    while (in.peek() != JsonToken.END_ARRAY)
                        requiredPacks.add(this.gson.fromJson(in, Key.class));
                    in.endArray();
                }
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }
        in.endObject();

        if (key == null) {
            throw new JsonParseException("The key field has not been specified");
        } else if (value == null) {
            throw new JsonParseException("The value field has not been specified");
        }

        return new DataEntry<>(key, value, requiredPacks);
    }
}