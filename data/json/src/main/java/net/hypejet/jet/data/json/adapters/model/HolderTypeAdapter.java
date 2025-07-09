package net.hypejet.jet.data.json.adapters.model;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonHolder holders}.
 *
 * @param <V> a value type of the holder that this type adapter converts
 * @since 1.0
 */
public final class HolderTypeAdapter<V> extends TypeAdapter<JsonHolder<V>> {

    private static final String VALUE_FIELD = "value";

    private final Gson gson;
    private final Class<V> valueClass;

    /**
     * Constructs the {@linkplain HolderTypeAdapter holder type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @param valueClass a value class of the holder that the holder type adapter should convert
     * @since 1.0
     */
    public HolderTypeAdapter(@NonNull Gson gson, @NonNull Class<V> valueClass) {
        this.gson = Objects.requireNonNull(gson, "gson");
        this.valueClass = Objects.requireNonNull(valueClass," value class");
    }

    @Override
    public void write(JsonWriter out, JsonHolder<V> value) throws IOException {
        switch (value) {
            case JsonHolder.Direct<V> direct -> {
                out.beginObject();
                out.name(VALUE_FIELD);
                this.gson.toJson(direct.value(), this.valueClass, out);
                out.endObject();
            }
            case JsonHolder.Reference<V> reference -> this.gson.toJson(reference.key(), Key.class, out);
        }
    }

    @Override
    public JsonHolder<V> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();

            if (!in.nextName().equals(VALUE_FIELD))
                throw new JsonParseException("Expected the direct holder to have only value field");
            V value = this.gson.fromJson(in, this.valueClass);

            in.endObject();
            return new JsonHolder.Direct<>(value);
        } else {
            return new JsonHolder.Reference<>(this.gson.fromJson(in, Key.class));
        }
    }
}